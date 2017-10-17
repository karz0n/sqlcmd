package ua.in.denoming.sqlcmd.model;

import ua.in.denoming.sqlcmd.model.exception.DatabaseException;

import java.util.ArrayList;

public interface DatabaseManager {
    void open(String url, String user, String password) throws DatabaseException;

    void close() throws DatabaseException;

    boolean isOpen();

    ArrayList<TableDescription> getTables() throws DatabaseException;

    void createTable(String tableName, String... columns) throws DatabaseException;

    void clearTable(String tableName) throws DatabaseException;

    void deleteTable(String tableName) throws DatabaseException;

    ArrayList<DataSet> obtainTableData(String tableName) throws DatabaseException;

    void insertData(String tableName, DataSet dataSet) throws DatabaseException;

    void updateData(String tableName, String column, String searchValue, String value) throws DatabaseException;

    void deleteData(String tableName, String column, String searchValue) throws DatabaseException;
}
