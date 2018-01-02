package ua.in.denoming.sqlcmd.model;

import org.apache.commons.lang3.Validate;

import ua.in.denoming.sqlcmd.model.exception.ConnectionRefusedException;
import ua.in.denoming.sqlcmd.model.exception.DatabaseException;
import ua.in.denoming.sqlcmd.model.exception.NotConnectedException;
import ua.in.denoming.sqlcmd.model.exception.WrongCredentialException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * JDBC database manager implementation
 */
public final class JdbcDatabaseManager implements DatabaseManager {
    private ErrorStates errorStates;
    private Connection connection;

    /**
     * Register drivers
     * @param drivers list of drivers separated by semicolon
     */
    public static void registerDrivers(String drivers) {
        Validate.notEmpty(drivers);

        try {
            String[] items = drivers.split(";");
            for (String item : items) {
                Class.forName(item);
            }
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Register drivers");
        }
    }

    /**
     * Construct class
     * @param errorStates error stats
     */
    public JdbcDatabaseManager(ErrorStates errorStates) {
        this.errorStates = errorStates;
    }

    /**
     * Open database connection
     *
     * @param url database url
     * @param user database user name
     * @param password database user password
     * @throws DatabaseException when open database connection error occurs
     */
    @Override
    public void open(String url, String user, String password) throws DatabaseException {
        Validate.notEmpty(url);
        Validate.notEmpty(user);
        Validate.notEmpty(password);

        try {
            if (isOpen()) {
                close();
            }

            connection = DriverManager.getConnection(url, getConnectionProperties(user, password));
        } catch (SQLException e) {
            String errorState = e.getSQLState();

            if (errorStates.isConnectionRefused(errorState)) {
                throw new ConnectionRefusedException(String.format("Connect to '%s' url has refused", url), e);
            }

            if (errorStates.isWrongPassword(errorState)) {
                throw new WrongCredentialException("Incorrect open connection user credential", e);
            }

            throw new DatabaseException(String.format("Open database connection by '%s' url", url), e);
        }
    }

    /**
     * Close database connection
     *
     * @throws DatabaseException when close database connection error occurs
     */
    @Override
    public void close() throws DatabaseException {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Close database connection", e);
        }
    }

    /**
     * Check is database connection established
     *
     * @return a connection to database status
     */
    @Override
    public boolean isOpen() {
        try {
            return (connection != null && !connection.isClosed());
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Get list of tables
     *
     * @return read only set of table description objects
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException     if there is database exception
     */
    @Override
    public Set<TableDescription> getTables() {
        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (
                ResultSet rs = metaData.getTables(
                    null, null, null, new String[]{"TABLE"})
            ) {
                Set<TableDescription> tables = new HashSet<>();
                while (rs.next()) {
                    TableDescription description = new TableDescription(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                    );
                    tables.add(description);
                }
                return Collections.unmodifiableSet(tables);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Get list of tables", e);
        }
    }

    /**
     * Create new table
     *
     * @param tableName name of table
     * @param columns   list of columns in table
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException     if there is database exception
     */
    @Override
    public void createTable(String tableName, String... columns) {
        Validate.notEmpty(tableName);
        Validate.isTrue(columns.length > 0);

        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String creatingString = JdbcDatabaseManager.getCreatingTableQueryString(tableName, columns);
            statement.executeUpdate(creatingString);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Create '%s' table", tableName), e);
        }
    }

    /**
     * Clear data of table
     *
     * @param tableName name of table
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException     if there is database exception
     */
    @Override
    public void clearTable(String tableName) {
        Validate.notEmpty(tableName);

        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String clearingString = JdbcDatabaseManager.getClearingTableQueryString(tableName);
            statement.executeUpdate(clearingString);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Clear '%s' table", tableName), e);
        }
    }

    /**
     * Delete table
     *
     * @param tableName name of table
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException     if there is database exception
     */
    @Override
    public void deleteTable(String tableName) {
        Validate.notEmpty(tableName);

        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String droppingString = JdbcDatabaseManager.getDroppingTableQueryString(tableName);
            statement.executeUpdate(droppingString);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Delete '%s' table", tableName), e);
        }
    }

    /**
     * Check table exists
     *
     * @param tableName name of table
     * @return result of table existence checking
     * @throws NotConnectedException if connection wasn't established
     */
    @Override
    public boolean isTableExists(String tableName) {
        Validate.notEmpty(tableName);

        if (!isOpen()) {
            throw new NotConnectedException();
        }

        Set<TableDescription> tables = getTables();
        boolean found = false;
        for (TableDescription table : tables) {
            if (table.getName().equalsIgnoreCase(tableName)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Get data of specific table
     *
     * @param tableName name of table
     * @return read only collection of data
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException     if there is database exception
     */
    @Override
    public List<DataSet> getData(String tableName) {
        Validate.notEmpty(tableName);

        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String receivingDataString = JdbcDatabaseManager.getReceivingDataQueryString(tableName);

            ResultSet rs = statement.executeQuery(receivingDataString);
            ResultSetMetaData metaData = rs.getMetaData();

            List<DataSet> tableData = new ArrayList<>();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                DataSet dataSet = new DataSet(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    dataSet.set(metaData.getColumnName(i), rs.getObject(i));
                }
                tableData.add(dataSet);
            }
            return Collections.unmodifiableList(tableData);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Get data of '%s' table", tableName), e);
        }
    }

    /**
     * Insert data to table
     *
     * @param tableName name of table
     * @param dataSet set of data
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException if there is database exception
     */
    @Override
    public void insertData(String tableName, DataSet dataSet) {
        Validate.notEmpty(tableName);
        Validate.isTrue(dataSet.size() > 0);

        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String insertingDataString = JdbcDatabaseManager.getInsertingDataQueryString(tableName, dataSet);
            statement.executeUpdate(insertingDataString);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Insert data to '%s' table", tableName), e);
        }
    }

    /**
     * Update data of table
     *
     * @param tableName name of table
     * @param columnName specific column name
     * @param searchValue value to search in specific column
     * @param value new value
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException if there is database exception
     */
    @Override
    public void updateData(String tableName, String columnName, String searchValue, String value) {
        Validate.notEmpty(tableName);
        Validate.notEmpty(columnName);
        Validate.notEmpty(searchValue);
        Validate.notNull(value);

        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String updatingDataString = JdbcDatabaseManager.getUpdatingDataQueryString(tableName, columnName, searchValue, value);
            statement.executeUpdate(updatingDataString);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Update data of '%s' table", tableName), e);
        }
    }

    /**
     * Delete data in specific table
     *
     * @param tableName name of table
     * @param columnName specific column name
     * @param searchValue value to search in specific column
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException if there is database exception
     */
    @Override
    public void deleteData(String tableName, String columnName, String searchValue) {
        Validate.notEmpty(tableName);
        Validate.notEmpty(columnName);
        Validate.notEmpty(searchValue);

        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String deletingDataString = JdbcDatabaseManager.getDeletingDataQueryString(tableName, columnName, searchValue);
            statement.executeUpdate(deletingDataString);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Delete data in '%s' table", tableName), e);
        }
    }

    private static String getCreatingTableQueryString(String tableName, String... columns) {
        StringBuilder builder = new StringBuilder("CREATE TABLE ");
        builder.append(tableName).append(" (");
        for (int i = 0; i < columns.length; ++i) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(columns[i]).append(" TEXT");
        }
        return builder.append(")").toString();
    }

    private static String getClearingTableQueryString(String tableName) {
        return "TRUNCATE ".concat(tableName);
    }

    private static String getDroppingTableQueryString(String tableName) {
        return "DROP TABLE ".concat(tableName);
    }

    private static String getReceivingDataQueryString(String tableName) {
        return "SELECT * FROM ".concat(tableName);
    }

    private static String getInsertingDataQueryString(String tableName, DataSet dataSet) {
        StringBuilder builder = new StringBuilder();

        int counter = 0;
        builder.append("INSERT INTO ").append(tableName).append('(');
        Set<String> names = dataSet.names();
        for (String name : names) {
            if (counter > 0) {
                builder.append(", ");
            }
            builder.append(name);
            counter++;
        }

        counter = 0;
        builder.append(") VALUES (");
        Collection<Object> values = dataSet.values();
        for (Object value : values) {
            if (counter > 0) {
                builder.append(", ");
            }
            builder.append('\'').append(value).append('\'');
            counter++;
        }
        builder.append(')');

        return builder.toString();
    }

    private static String getUpdatingDataQueryString(String tableName, String column, String searchValue, String value) {
        return String.format("UPDATE %s SET %s='%s' WHERE %s='%s'", tableName, column, value, column, searchValue);
    }

    private static String getDeletingDataQueryString(String tableName, String column, String searchValue) {
        return String.format("DELETE FROM %s WHERE %s='%s'", tableName, column, searchValue);
    }

    private Properties getConnectionProperties(String username, String password) {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("loggerLevel", "OFF");
        return props;
    }
}
