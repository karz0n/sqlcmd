package ua.in.denoming.sqlcmd.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcDatabaseManagerTest {
    private DatabaseManager databaseManager;

    @BeforeAll
    void setup() {
        databaseManager = new JdbcDatabaseManager(
            new PostgreSqlErrorStates(), "org.postgresql.Driver"
        );
        try {
            databaseManager.open("jdbc:postgresql://192.168.1.20/sqlcmd", "admin", "nirvana311");
        } catch (Throwable throwable) {
            fail(throwable);
        }
    }

    @AfterAll
    void cleanup() {
        try {
            databaseManager.close();
        } catch (Throwable throwable) {
            fail(throwable);
        }
    }

    @Test
    @DisplayName("database has opened")
    void isOpen() {
        assertTrue(databaseManager.isOpen());
    }

    @Nested
    @DisplayName("after creating table")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class AfterTableCreating {
        private final String TABLE_NAME = "TEST_TABLE";
        private final DataSet TABLE_DATA = new DataSet(
            new DataSet.Data("column1", "value1"),
            new DataSet.Data("column2", "value2")
        );

        @BeforeAll
        void createTable() {
            try {
                databaseManager.createTable(TABLE_NAME, "column1", "column2");
                assertTrue(databaseManager.isTableExists(TABLE_NAME));
            } catch (Throwable throwable) {
                fail(throwable);
            }
        }

        @AfterAll
        void deleteTable() {
            try {
                databaseManager.deleteTable(TABLE_NAME);
                assertFalse(databaseManager.isTableExists(TABLE_NAME));
            } catch (Throwable throwable) {
                fail(throwable);
            }
        }

        @Test
        @DisplayName("table exists")
        void isTableExists() {
            try {
                assertTrue(databaseManager.isTableExists(TABLE_NAME));
            } catch (Throwable throwable) {
                fail(throwable);
            }
        }

        @Nested
        @DisplayName("after inserting data")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class AfterDataInserting {
            @BeforeAll
            void insertData() {
                try {
                    databaseManager.insertData(TABLE_NAME, TABLE_DATA);
                } catch (Throwable throwable) {
                    fail(throwable);
                }
            }

            @Test
            @DisplayName("data have inserted")
            void isDataExists() {
                try {
                    ArrayList<DataSet> dataSets = databaseManager.obtainTableData(TABLE_NAME);
                    assertEquals(1, dataSets.size());
                    assertEquals(dataSets.get(0), TABLE_DATA);
                } catch (Throwable throwable) {
                    fail(throwable);
                }
            }

            @Test
            @DisplayName("data have updated")
            void testUpdateData() {
                try {
                    String column = TABLE_DATA.get(0).getName();
                    String searchValue = TABLE_DATA.getString(0);
                    String value = "value";

                    databaseManager.updateData(
                        TABLE_NAME,
                        column,
                        searchValue,
                        value
                    );

                    ArrayList<DataSet> dataSets = databaseManager.obtainTableData(TABLE_NAME);
                    assertEquals(1, dataSets.size());

                    DataSet dataSet = new DataSet(
                        new DataSet.Data(column, value),
                        new DataSet.Data(TABLE_DATA.get(1).getName(), TABLE_DATA.get(1).getValue())
                    );
                    assertEquals(dataSets.get(0), dataSet);
                } catch (Throwable throwable) {
                    fail(throwable);
                }
            }

            @Test
            @DisplayName("data have deleted")
            void testDeleteData() {
                try {
                    String column = TABLE_DATA.get(1).getName();
                    String searchValue = TABLE_DATA.getString(1);

                    databaseManager.deleteData(TABLE_NAME, column, searchValue);

                    ArrayList<DataSet> dataSets = databaseManager.obtainTableData(TABLE_NAME);
                    assertEquals(0, dataSets.size());
                } catch (Throwable throwable) {
                    fail(throwable);
                }
            }
        }

    }
}
