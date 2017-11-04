package ua.in.denoming.sqlcmd.model;

import ua.in.denoming.sqlcmd.model.exception.DatabaseException;
import ua.in.denoming.sqlcmd.model.exception.DatabaseNotFoundException;
import ua.in.denoming.sqlcmd.model.exception.WrongPasswordException;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

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

    @Override
    public ArrayList<TableDescription> getTables() throws DatabaseException {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (
                ResultSet rs = metaData.getTables(
                    null, null, null,
                    new String[]{"TABLE"})
            ) {
                ArrayList<TableDescription> output = new ArrayList<>();
                while (rs.next()) {
                    TableDescription description = new TableDescription(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                    );
                    output.add(description);
                }
                return output;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Obtain database meta data", e);
        }
    }

    @Override
    public void createTable(String tableName, String... columns) throws DatabaseException {
        try (
            Statement statement = connection.createStatement()
        ) {
            String creatingString = JdbcDatabaseManager.getCreatingTableString(tableName, columns);
            statement.executeUpdate(creatingString);
        } catch (SQLException e) {
            throw new DatabaseException("Create table", e);
        }
    }

    @Override
    public void clearTable(String tableName) throws DatabaseException {
        try (
            Statement statement = connection.createStatement()
        ) {
            String clearingString = JdbcDatabaseManager.getClearingTableString(tableName);
            statement.executeUpdate(clearingString);
        } catch (SQLException e) {
            throw new DatabaseException("Truncate table", e);
        }
    }

    @Override
    public void deleteTable(String tableName) throws DatabaseException {
        try (
            Statement statement = connection.createStatement()
        ) {
            String droppingString = JdbcDatabaseManager.getDroppingTableString(tableName);
            statement.executeUpdate(droppingString);
        } catch (SQLException e) {
            throw new DatabaseException("Drop table", e);
        }
    }

    @Override
    public boolean isTableExists(String tableName) throws DatabaseException {
        ArrayList<TableDescription> tables = getTables();
        boolean found = false;
        for (TableDescription table: tables) {
            if (table.getName().equalsIgnoreCase(tableName)) {
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    public ArrayList<DataSet> obtainTableData(String tableName) throws DatabaseException {
        try (
            Statement statement = connection.createStatement()
        ) {
            String receivingDataString = JdbcDatabaseManager.getReceivingDataString(tableName);

            ResultSet rs = statement.executeQuery(receivingDataString);
            ResultSetMetaData metaData = rs.getMetaData();

            ArrayList<DataSet> output = new ArrayList<>();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                DataSet dataSet = new DataSet(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    dataSet.put(
                        metaData.getColumnName(i), rs.getObject(i)
                    );
                }
                output.add(dataSet);
            }
            return output;
        } catch (SQLException e) {
            throw new DatabaseException("Fetch table data", e);
        }
    }

    @Override
    public void insertData(String tableName, DataSet dataSet) throws DatabaseException {
        try (
            Statement statement = connection.createStatement()
        ) {
            String insertingDataString = JdbcDatabaseManager.getInsertingDataString(tableName, dataSet);
            statement.executeUpdate(insertingDataString);
        } catch (SQLException e) {
            throw new DatabaseException("Insert data", e);
        }
    }

    @Override
    public void updateData(String tableName, String column, String searchValue, String value) throws DatabaseException {
        try (
            Statement statement = connection.createStatement()
        ) {
            String updatingDataString = JdbcDatabaseManager.getUpdatingDataString(tableName, column, searchValue, value);
            statement.executeUpdate(updatingDataString);
        } catch (SQLException e) {
            throw new DatabaseException("Update data", e);
        }
    }

    @Override
    public void deleteData(String tableName, String column, String searchValue) throws DatabaseException {
        try (
            Statement statement = connection.createStatement()
        ) {
            String deletingDataString = JdbcDatabaseManager.getDeletingDataString(tableName, column, searchValue);
            statement.executeUpdate(deletingDataString);
        } catch (SQLException e) {
            throw new DatabaseException("Drop row in table", e);
        }
    }

    private static String getCreatingTableString(String tableName, String... columns) {
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

    private static String getClearingTableString(String tableName) {
        return "TRUNCATE ".concat(tableName);
    }

    private static String getDroppingTableString(String tableName) {
        return "DROP TABLE ".concat(tableName);
    }

    private static String getReceivingDataString(String tableName) {
        return "SELECT * FROM ".concat(tableName);
    }

    private static String getInsertingDataString(String tableName, DataSet dataSet) {
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

    private static String getUpdatingDataString(String tableName, String column, String searchValue, String value) {
        return String.format(
            "UPDATE %s SET %s='%s' WHERE %s='%s'", tableName, column, value, column, searchValue
        );
    }

    private static String getDeletingDataString(String tableName, String column, String searchValue) {
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
