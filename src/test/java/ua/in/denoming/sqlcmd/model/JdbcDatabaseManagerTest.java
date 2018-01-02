package ua.in.denoming.sqlcmd.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ua.in.denoming.sqlcmd.model.exception.ConnectionRefusedException;
import ua.in.denoming.sqlcmd.model.exception.WrongCredentialException;
import ua.in.denoming.sqlcmd.TestProperties;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcDatabaseManagerTest {
    private static TestProperties testProperties = new TestProperties();

    private DatabaseManager databaseManager = new JdbcDatabaseManager(
        new PostgreSqlErrorStates()
    );

    @BeforeAll
    void setup() {
        JdbcDatabaseManager.registerDrivers("org.postgresql.Driver");
    }

    @Test
    void testOpenState() {
        assertFalse(databaseManager.isOpen());
    }

    @Test
    void testWrongDatabaseOpening() {
        assertThrows(ConnectionRefusedException.class, () -> databaseManager.open(
            "jdbc:postgresql://localhost/missing_database",
            "someName",
            "somePassword"
        ));
    }

    @Test
    void testWrongCredential() {
        assertThrows(WrongCredentialException.class, () -> databaseManager.open(
            JdbcDatabaseManagerTest.testProperties.getDatabaseUrl(),
            "someName",
            "somePassword"
        ));
    }

    @Nested
    @DisplayName("after database opening")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class AfterDatabaseOpening {
        @BeforeAll
        void setup() {
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
        void testOpenState() {
            assertTrue(databaseManager.isOpen());
        }

        @Nested
        @DisplayName("after table creating")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class AfterTableCreating {
            private final String TABLE_NAME = "TEST_TABLE";

            @BeforeAll
            void setup() {
                databaseManager.createTable(TABLE_NAME, "column1", "column2", "column3");
            }

            @AfterAll
            void cleanup() {
                databaseManager.deleteTable(TABLE_NAME);
            }

            @Test
            void isTableExists() {
                assertTrue(databaseManager.isTableExists(TABLE_NAME));
            }

            @Nested
            @DisplayName("after inserting data")
            @TestInstance(TestInstance.Lifecycle.PER_CLASS)
            class AfterDataInserting {
                private final DataSet TABLE_DATA = new DataSet(Arrays.asList(
                    new AbstractMap.SimpleEntry<>("column1", "value1"),
                    new AbstractMap.SimpleEntry<>("column2", "value2"),
                    new AbstractMap.SimpleEntry<>("column3", "value3")
                ));

                @Test
                void testInsertData() {
                    databaseManager.insertData(TABLE_NAME, TABLE_DATA);

                    List<DataSet> insertedTableData = databaseManager.getData(TABLE_NAME);
                    assertEquals(1, insertedTableData.size());
                    assertEquals(insertedTableData.get(0), TABLE_DATA);

                    databaseManager.clearTable(TABLE_NAME);
                }

                @Test
                void testUpdateData() {
                    databaseManager.insertData(TABLE_NAME, TABLE_DATA);

                    Iterator<Map.Entry<String, Object>> iterator = TABLE_DATA.iterator();

                    Map.Entry<String, Object> firstEntry = iterator.next();
                    String columnName = firstEntry.getKey();
                    String searchValue = firstEntry.getValue().toString();
                    String value = "value";
                    databaseManager.updateData(
                        TABLE_NAME,
                        columnName,
                        searchValue,
                        value
                    );

                    DataSet dataSet = new DataSet();
                    dataSet.set(columnName, value);
                    while (iterator.hasNext()) {
                        Map.Entry<String, Object> entry = iterator.next();
                        dataSet.set(entry.getKey(), entry.getValue());
                    }

                    List<DataSet> tableData = databaseManager.getData(TABLE_NAME);
                    assertEquals(1, tableData.size());
                    assertEquals(tableData.get(0), dataSet);

                    databaseManager.clearTable(TABLE_NAME);
                }

                @Test
                void testDeleteData() {
                    databaseManager.insertData(TABLE_NAME, TABLE_DATA);

                    Map.Entry<String, Object> firstEntry = TABLE_DATA.iterator().next();
                    String column = firstEntry.getKey();
                    String searchValue = firstEntry.getValue().toString();
                    databaseManager.deleteData(
                        TABLE_NAME,
                        column,
                        searchValue
                    );

                    List<DataSet> dataSets = databaseManager.getData(TABLE_NAME);
                    assertEquals(0, dataSets.size());
                }
            }
        }
    }
}
