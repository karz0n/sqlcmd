package ua.in.denoming.sqlcmd.model;

import ua.in.denoming.sqlcmd.model.exception.DatabaseException;
import ua.in.denoming.sqlcmd.model.exception.DatabaseNotFoundException;
import ua.in.denoming.sqlcmd.model.exception.NotConnectedException;
import ua.in.denoming.sqlcmd.model.exception.WrongPasswordException;

import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 */
public final class JdbcDatabaseManager implements DatabaseManager {
    private ErrorStates errorStates;
    private Connection connection;

    @SuppressWarnings("unused")
    public JdbcDatabaseManager(ErrorStates errorStates) {
        this(errorStates, null, null);
    }

    public JdbcDatabaseManager(ErrorStates errorStates, String drivers) {
        this(errorStates, drivers, null);
    }

    @SuppressWarnings("WeakerAccess")
    public JdbcDatabaseManager(ErrorStates errorStates, String drivers, PrintWriter writer) {
        this.errorStates = errorStates;
        configure(drivers, writer);
    }

    @Override
    public void open(String url, String user, String password) throws DatabaseException {
        try {
            if (isOpen()) {
                connection.close();
            }

            connection = DriverManager.getConnection(url, getConnectionProperties(user, password));
        } catch (SQLException e) {
            String errorState = e.getSQLState();

            if (errorStates.isWrongPassword(errorState)) {
                throw new WrongPasswordException();
            }

            if (errorStates.isDatabaseNotFound(errorState)) {
                throw new DatabaseNotFoundException();
            }

            throw new DatabaseException("Open database connection", e);
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Close database connection", e);
        }
    }

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
     * @return list of tables
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
                return tables;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Obtain database meta data", e);
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
        if (!isOpen()) {
            throw new NotConnectedException();
        }
        try (
            Statement statement = connection.createStatement()
        ) {
            String creatingString = JdbcDatabaseManager.getCreatingTableQueryString(tableName, columns);
            statement.executeUpdate(creatingString);
        } catch (SQLException e) {
            throw new DatabaseException("Create table", e);
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
        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String clearingString = JdbcDatabaseManager.getClearingTableQueryString(tableName);
            statement.executeUpdate(clearingString);
        } catch (SQLException e) {
            throw new DatabaseException("Truncate table", e);
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
        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String droppingString = JdbcDatabaseManager.getDroppingTableQueryString(tableName);
            statement.executeUpdate(droppingString);
        } catch (SQLException e) {
            throw new DatabaseException("Drop table", e);
        }
    }

    /**
     * Check table exists
     * @param tableName name of table
     * @return result of table existence checking
     * @throws NotConnectedException if connection wasn't established
     */
    @Override
    public boolean isTableExists(String tableName) {
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
     * Obtain list of table data sets
     * @param tableName name of table
     * @return set of data
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException     if there is database exception
     */
    @Override
    public List<DataSet> obtainTableData(String tableName) {
        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String receivingDataString = JdbcDatabaseManager.getReceivingDataQueryString(tableName);

            ResultSet rs = statement.executeQuery(receivingDataString);
            ResultSetMetaData metaData = rs.getMetaData();

            List<DataSet> dataSets = new ArrayList<>();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                DataSet dataSet = new DataSet(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    dataSet.put(
                        metaData.getColumnName(i), rs.getObject(i)
                    );
                }
                dataSets.add(dataSet);
            }
            return dataSets;
        } catch (SQLException e) {
            throw new DatabaseException("Fetch table data", e);
        }
    }

    /**
     * Insert data set to table
     * @param tableName name of table
     * @param dataSet set of data
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException     if there is database exception
     */
    @Override
    public void insertData(String tableName, DataSet dataSet) {
        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String insertingDataString = JdbcDatabaseManager.getInsertingDataQueryString(tableName, dataSet);
            statement.executeUpdate(insertingDataString);
        } catch (SQLException e) {
            throw new DatabaseException("Insert data", e);
        }
    }

    /**
     * Update table
     * @param tableName name of table
     * @param column specific column name
     * @param searchValue value to search in specific column
     * @param value new value
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException     if there is database exception
     */
    @Override
    public void updateData(String tableName, String column, String searchValue, String value) {
        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String updatingDataString = JdbcDatabaseManager.getUpdatingDataQueryString(tableName, column, searchValue, value);
            statement.executeUpdate(updatingDataString);
        } catch (SQLException e) {
            throw new DatabaseException("Update data", e);
        }
    }

    /**
     * Delete data in table
     * @param tableName name of table
     * @param column specific column name
     * @param searchValue value to search in specific column
     * @throws NotConnectedException if connection wasn't established
     * @throws DatabaseException     if there is database exception
     */
    @Override
    public void deleteData(String tableName, String column, String searchValue) {
        if (!isOpen()) {
            throw new NotConnectedException();
        }

        try (
            Statement statement = connection.createStatement()
        ) {
            String deletingDataString = JdbcDatabaseManager.getDeletingDataQueryString(tableName, column, searchValue);
            statement.executeUpdate(deletingDataString);
        } catch (SQLException e) {
            throw new DatabaseException("Drop row in table", e);
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
        builder.append(")");
        return builder.toString();
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
        StringBuilder builder = new StringBuilder("INSERT INTO ");

        builder.append(tableName).append('(');
        String[] names = dataSet.getNames();
        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(names[i]);
        }

        builder.append(") VALUES (");

        Object[] objects = dataSet.getValues();
        for (int i = 0; i < objects.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append('\'').append(objects[i]).append('\'');
        }
        builder.append(')');

        return builder.toString();
    }

    private static String getUpdatingDataQueryString(String tableName, String column, String searchValue, String value) {
        return String.format(
            "UPDATE %s SET %s='%s' WHERE %s='%s'", tableName, column, value, column, searchValue
        );
    }

    private static String getDeletingDataQueryString(String tableName, String column, String searchValue) {
        return String.format(
            "DELETE FROM %s WHERE %s='%s'", tableName, column, searchValue
        );
    }

    private void configure(String drivers, PrintWriter writer) {
        if (drivers != null) {
            System.setProperty("jdbc.drivers", drivers);
        }
        DriverManager.setLogWriter(writer);
    }

    private Properties getConnectionProperties(String username, String password) {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("loggerLevel", "OFF");
        return props;
    }
}
