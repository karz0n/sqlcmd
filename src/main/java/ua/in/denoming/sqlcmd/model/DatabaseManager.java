package ua.in.denoming.sqlcmd.model;

import java.util.ArrayList;

public interface DatabaseManager {
    void open(String url, String user, String password);

    void close();

    boolean isOpen();

    ArrayList<TableDescription> getTables();

    void createTable(String tableName, String... columns);

    void clearTable(String tableName);

    void deleteTable(String tableName);

    boolean isTableExists(String tableName);

    ArrayList<DataSet> obtainTableData(String tableName);

    void insertData(String tableName, DataSet dataSet);

    void updateData(String tableName, String column, String searchValue, String value);

    void deleteData(String tableName, String column, String searchValue);
}
