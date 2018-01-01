package ua.in.denoming.sqlcmd.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ua.in.denoming.sqlcmd.TestProperties;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcDatabaseManagerTest {
    private static TestProperties testProperties = new TestProperties();

    private DatabaseManager databaseManager;

    @BeforeAll
    void setup() {
        JdbcDatabaseManager.registerDrivers("org.postgresql.Driver");

        databaseManager = new JdbcDatabaseManager(new PostgreSqlErrorStates());
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

        @BeforeAll
        void createTable() {
            databaseManager.createTable(TABLE_NAME, "column1", "column2", "column3");
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
            private DataSet tableData;

            @BeforeAll
            void insertData() {
                tableData = new DataSet();
                tableData.set("column1", "value1");
                tableData.set("column2", "value2");
                tableData.set("column3", "value3");

                databaseManager.insertData(TABLE_NAME, tableData);
            }

            @Test
            @DisplayName("data have inserted")
            void isDataExists() {
                List<DataSet> dataSets = databaseManager.getData(TABLE_NAME);
                assertEquals(1, dataSets.size());
                assertEquals(dataSets.get(0), tableData);
            }

            @Test
            @DisplayName("data have updated")
            void testUpdateData() {
                Iterator<Map.Entry<String, Object>> iterator = tableData.iterator();

                Map.Entry<String, Object> firstEntry = iterator.next();
                String column = firstEntry.getKey();
                String searchValue = firstEntry.getValue().toString();
                String value = "value";

                databaseManager.updateData(
                    TABLE_NAME,
                    column,
                    searchValue,
                    value
                );

                List<DataSet> tableData = databaseManager.getData(TABLE_NAME);
                assertEquals(1, tableData.size());

                DataSet dataSet = new DataSet();
                dataSet.set(column, value);
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    dataSet.set(entry.getKey(), entry.getValue());
                }

                assertEquals(tableData.get(0), dataSet);
            }

            @Test
            @DisplayName("data have deleted")
            void testDeleteData() {
                Map.Entry<String, Object> firstEntry = tableData.iterator().next();

                String column = firstEntry.getKey();
                String searchValue = firstEntry.getValue().toString();

                databaseManager.deleteData(TABLE_NAME, column, searchValue);

                List<DataSet> dataSets = databaseManager.getData(TABLE_NAME);
                assertEquals(1, dataSets.size());
            }
        }
    }
}
