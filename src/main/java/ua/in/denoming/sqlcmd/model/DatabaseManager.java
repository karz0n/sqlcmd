package ua.in.denoming.sqlcmd.model;

import java.util.Set;
import java.util.List;

public interface DatabaseManager {
    void open(String url, String user, String password);

    void close();

    boolean isOpen();

    Set<TableDescription> getTables();

    void createTable(String tableName, String... columns);

    void clearTable(String tableName);

    void deleteTable(String tableName);

    boolean isTableExists(String tableName);

    List<DataSet> getData(String tableName);

    void insertData(String tableName, DataSet dataSet);

    void updateData(String tableName, String column, String searchValue, String value);

    void deleteData(String tableName, String column, String searchValue);
}
