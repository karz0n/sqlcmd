package ua.in.denoming.sqlcmd.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ua.in.denoming.sqlcmd.TestProperties;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcDatabaseManagerTest {
    private static TestProperties testProperties = new TestProperties();

    private DatabaseManager databaseManager;

    @BeforeAll
    void setup() {
        databaseManager = new JdbcDatabaseManager(
            new PostgreSqlErrorStates(), "org.postgresql.Driver"
        );
        databaseManager.open(
            JdbcDatabaseManagerTest.testProperties.getDatabaseUrl(),
            JdbcDatabaseManagerTest.testProperties.getDatabaseUserName(),
            JdbcDatabaseManagerTest.testProperties.getDatabasePassword()
        );
    }

    @AfterAll
    void cleanup() {
        databaseManager.close();
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
            databaseManager.createTable(TABLE_NAME, "column1", "column2");
            assertTrue(databaseManager.isTableExists(TABLE_NAME));
        }

        @AfterAll
        void deleteTable() {
            databaseManager.deleteTable(TABLE_NAME);
            assertFalse(databaseManager.isTableExists(TABLE_NAME));
        }

        @Test
        @DisplayName("table exists")
        void isTableExists() {
            assertTrue(databaseManager.isTableExists(TABLE_NAME));
        }

        @Nested
        @DisplayName("after inserting data")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class AfterDataInserting {
            @BeforeAll
            void insertData() {
                databaseManager.insertData(TABLE_NAME, TABLE_DATA);
            }

            @Test
            @DisplayName("data have inserted")
            void isDataExists() {
                ArrayList<DataSet> dataSets = databaseManager.obtainTableData(TABLE_NAME);
                assertEquals(1, dataSets.size());
                assertEquals(dataSets.get(0), TABLE_DATA);
            }

            @Test
            @DisplayName("data have updated")
            void testUpdateData() {
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
            }

            @Test
            @DisplayName("data have deleted")
            void testDeleteData() {
                String column = TABLE_DATA.get(1).getName();
                String searchValue = TABLE_DATA.getString(1);

                databaseManager.deleteData(TABLE_NAME, column, searchValue);

                ArrayList<DataSet> dataSets = databaseManager.obtainTableData(TABLE_NAME);
                assertEquals(0, dataSets.size());
            }
        }

    }
}
