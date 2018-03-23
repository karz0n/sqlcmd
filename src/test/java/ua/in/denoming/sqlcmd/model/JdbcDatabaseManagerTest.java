package ua.in.denoming.sqlcmd.model;

import org.junit.jupiter.api.*;

import ua.in.denoming.sqlcmd.model.exception.WrongCredentialException;
import ua.in.denoming.sqlcmd.DatabaseProperties;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test JDBC database manager class")
class JdbcDatabaseManagerTest {
    private static DatabaseProperties databaseProperties = new DatabaseProperties();

    private DatabaseManager databaseManager = new JdbcDatabaseManager(
        new PostgreSqlErrorStates()
    );

    @BeforeAll
    void setup() {
        JdbcDatabaseManager.registerDrivers("org.postgresql.Driver");
    }

    @Test
    @DisplayName("Test database is closed")
    void testDatabaseIsClosed() {
        assertFalse(databaseManager.isOpen());
    }

    @Test
    @DisplayName("Test connect with wrong database name")
    void testWrongDatabase() {
        assertThrows(WrongCredentialException.class, () -> databaseManager.open(
            "jdbc:postgresql://localhost/missing_database",
            "someName",
            "somePassword"
        ));
    }

    @Test
    @DisplayName("Test connect with wrong credential")
    void testWrongCredential() {
        assertThrows(WrongCredentialException.class, () -> databaseManager.open(
            JdbcDatabaseManagerTest.databaseProperties.getDatabaseUrl(),
            "someName",
            "somePassword"
        ));
    }

    @Nested
    @DisplayName("After database was opened")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class AfterDatabaseOpening {
        @BeforeAll
        void setup() {
            databaseManager.open(
                JdbcDatabaseManagerTest.databaseProperties.getDatabaseUrl(),
                JdbcDatabaseManagerTest.databaseProperties.getDatabaseUserName(),
                JdbcDatabaseManagerTest.databaseProperties.getDatabasePassword()
            );
        }

        @AfterAll
        void cleanup() {
            databaseManager.close();
        }

        @Test
        @DisplayName("Test database is opened")
        void testDatabaseIsOpened() {
            assertTrue(databaseManager.isOpen());
        }

        @Nested
        @DisplayName("After table was created")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class AfterTableCreating {
            private final String TABLE_NAME = "TEST_TABLE";
            private final DataSet TABLE_DATA = new DataSet(Arrays.asList(
                    new AbstractMap.SimpleEntry<>("column1", "value1"),
                    new AbstractMap.SimpleEntry<>("column2", "value2"),
                    new AbstractMap.SimpleEntry<>("column3", "value3")
            ));

            @BeforeAll
            void setup() {
                databaseManager.createTable(TABLE_NAME, "column1", "column2", "column3");
            }

            @AfterAll
            void cleanup() {
                databaseManager.deleteTable(TABLE_NAME);
            }

            @Test
            @DisplayName("Test table exists")
            void isTableExists() {
                assertTrue(databaseManager.isTableExists(TABLE_NAME));
            }

            @Test
            @DisplayName("Test table is empty")
            void isTableEmpty() {
                assertTrue(databaseManager.getData(TABLE_NAME).size() == 0);
            }

            @Test
            @DisplayName("Test of data insertion")
            void testInsertData() {
                databaseManager.insertData(TABLE_NAME, TABLE_DATA);

                List<DataSet> insertedTableData = databaseManager.getData(TABLE_NAME);
                assertEquals(1, insertedTableData.size());
                assertEquals(insertedTableData.get(0), TABLE_DATA);

                databaseManager.clearTable(TABLE_NAME);
            }

            @Nested
            @DisplayName("After data was inserted")
            @TestInstance(TestInstance.Lifecycle.PER_CLASS)
            class AfterDataInserting {
                @BeforeEach
                void insertData() {
                    databaseManager.insertData(TABLE_NAME, TABLE_DATA);
                }

                @AfterEach
                void clearData() {
                    databaseManager.clearTable(TABLE_NAME);
                }

                @Test
                @DisplayName("Test of data updating")
                void testUpdateData() {
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
                }

                @Test
                @DisplayName("Test of data deleting")
                void testDeleteData() {
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
