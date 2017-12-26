package ua.in.denoming.sqlcmd.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.Main;
import ua.in.denoming.sqlcmd.TestProperties;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationTest {
    private static final String TABLE_NAME = "TEST_TABLE";

    private static TestProperties testProperties = new TestProperties();

    private ConfigurableInputStream in;
    private LogOutputStream out;
    private String connectionString;

    @BeforeEach
    void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        connectionString = IntegrationTest.testProperties.getDatabaseConnectionString();
    }

    @Test
    void testHelp() {
        in.writeLine("help");
        in.writeLine("exit");

        Main.main(new String[0]);

        String expected = "Hello, type 'help' to get help or 'exit' to quit from program" + System.lineSeparator() +
            "Usage: <command> [<param1> <param2> ...]" + System.lineSeparator() +
            "\tCommands:" + System.lineSeparator() +
            "\t\tconnect\t<url> <username> <password>" + System.lineSeparator() +
            "\t\t\tconnect to database" + System.lineSeparator() +
            "\t\tgetTables" + System.lineSeparator() +
            "\t\t\tprint list of getTables" + System.lineSeparator() +
            "\t\tclear\t<tableName>" + System.lineSeparator() +
            "\t\t\tclear specified table" + System.lineSeparator() +
            "\t\tdrop\t<tableName>" + System.lineSeparator() +
            "\t\t\tdeleteData specified table" + System.lineSeparator() +
            "\t\tcreate\t<tableName> <column1:type> <column2:type> ..." + System.lineSeparator() +
            "\t\t\tcreate table using specified column names and types" + System.lineSeparator() +
            "\t\tfind\t<tableName>" + System.lineSeparator() +
            "\t\t\tprint content of specified table" + System.lineSeparator() +
            "\t\tinsertData\t<tableName> <column1> <value1> <column2> <value2> ..." + System.lineSeparator() +
            "\t\t\tinsertData row with specified data" + System.lineSeparator() +
            "\t\tupdateData\t<tableName> <column1> <value1> <column2> <value2>" + System.lineSeparator() +
            "\t\t\tupdateData rows where <column1> equal <value1> and set <column2> to <value2>" + System.lineSeparator() +
            "\t\tdeleteData\t<tableName> <column> <value>" + System.lineSeparator() +
            "\t\t\tdeleteData specified table where <column> equal <value>" + System.lineSeparator() +
            "\t\thelp" + System.lineSeparator() +
            "\t\t\tprint this help" + System.lineSeparator() +
            "\t\texit" + System.lineSeparator() +
            "\t\t\tquit from program" + System.lineSeparator() +
            "Goodbye, see you later" + System.lineSeparator();
        assertEquals(expected, out.readLine());
    }

    @Test
    void testExit() {
        in.writeLine("exit");

        Main.main(new String[0]);

        String expected = "Hello, type 'help' to get help or 'exit' to quit from program" + System.lineSeparator() +
            "Goodbye, see you later" + System.lineSeparator();
        assertEquals(expected, out.readLine());
    }

    @Test
    void testListTablesWithoutSuccessConnection() {
        in.writeLine("tables");
        in.writeLine("exit");

        Main.main(new String[0]);

        String expected = "Hello, type 'help' to get help or 'exit' to quit from program" + System.lineSeparator() +
            "First need to establish connection" + System.lineSeparator() +
            "Goodbye, see you later" + System.lineSeparator();
        assertEquals(expected, out.readLine());
    }

    @Test
    void testConnect() {
        in.writeLine("connect " + connectionString);
        in.writeLine("exit");

        Main.main(new String[0]);

        String expected = "Hello, type 'help' to get help or 'exit' to quit from program" + System.lineSeparator() +
            "Database has opened successfully" + System.lineSeparator() +
            "Goodbye, see you later" + System.lineSeparator();
        assertEquals(expected, out.readLine());
    }

    @Test
    void testCreateWithDrop() {
        in.writeLine("connect " + connectionString);
        in.writeLine("create " + TABLE_NAME + " column1 column2");
        in.writeLine("drop " + TABLE_NAME);
        in.writeLine("exit");

        Main.main(new String[0]);

        String expected = "Hello, type 'help' to get help or 'exit' to quit from program" + System.lineSeparator() +
            "Database has opened successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has created successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has dropped successfully" + System.lineSeparator() +
            "Goodbye, see you later" + System.lineSeparator();
        assertEquals(expected, out.readLine());
    }

    @Test
    void testInsertWithFind() {
        in.writeLine("connect " + connectionString);
        in.writeLine("create " + TABLE_NAME + " column1 column2");
        in.writeLine("insert " + TABLE_NAME + " column1 value1 column2 value2");
        in.writeLine("find " + TABLE_NAME);
        in.writeLine("drop " + TABLE_NAME);
        in.writeLine("exit");

        Main.main(new String[0]);

        String expected = "Hello, type 'help' to get help or 'exit' to quit from program" + System.lineSeparator() +
            "Database has opened successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has created successfully" + System.lineSeparator() +
            "Values to '" + TABLE_NAME + "' table has inserted successfully" + System.lineSeparator() +
            "+------------+------------+" + System.lineSeparator() +
            "|  column1   |  column2   |" + System.lineSeparator() +
            "+------------+------------+" + System.lineSeparator() +
            "|   value1   |   value2   |" + System.lineSeparator() +
            "+------------+------------+" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has dropped successfully" + System.lineSeparator() +
            "Goodbye, see you later" + System.lineSeparator();
        assertEquals(expected, out.readLine());
    }

    @Test
    void testUpdate() {
        in.writeLine("connect " + connectionString);
        in.writeLine("create " + TABLE_NAME + " column1 column2");
        in.writeLine("insert " + TABLE_NAME + " column1 value1 column2 value2");
        in.writeLine("update " + TABLE_NAME + " column1 value1 value3");
        in.writeLine("find " + TABLE_NAME);
        in.writeLine("drop " + TABLE_NAME);
        in.writeLine("exit");

        Main.main(new String[0]);

        String expected = "Hello, type 'help' to get help or 'exit' to quit from program" + System.lineSeparator() +
            "Database has opened successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has created successfully" + System.lineSeparator() +
            "Values to '" + TABLE_NAME + "' table has inserted successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has updated successfully" + System.lineSeparator() +
            "+------------+------------+" + System.lineSeparator() +
            "|  column1   |  column2   |" + System.lineSeparator() +
            "+------------+------------+" + System.lineSeparator() +
            "|   value3   |   value2   |" + System.lineSeparator() +
            "+------------+------------+" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has dropped successfully" + System.lineSeparator() +
            "Goodbye, see you later" + System.lineSeparator();
        assertEquals(expected, out.readLine());
    }

    @Test
    void testDelete() {
        in.writeLine("connect " + connectionString);
        in.writeLine("create " + TABLE_NAME + " column1 column2");
        in.writeLine("insert " + TABLE_NAME + " column1 value1 column2 value2");
        in.writeLine("delete " + TABLE_NAME + " column1 value2");
        in.writeLine("drop " + TABLE_NAME);
        in.writeLine("exit");

        Main.main(new String[0]);

        String expected = "Hello, type 'help' to get help or 'exit' to quit from program" + System.lineSeparator() +
            "Database has opened successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has created successfully" + System.lineSeparator() +
            "Values to '" + TABLE_NAME + "' table has inserted successfully" + System.lineSeparator() +
            "Data in table '" + TABLE_NAME + "' has deleted successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has dropped successfully" + System.lineSeparator() +
            "Goodbye, see you later" + System.lineSeparator();
        assertEquals(expected, out.readLine());
    }

    @Test
    void testClear() {
        in.writeLine("connect " + connectionString);
        in.writeLine("create " + TABLE_NAME + " column1 column2");
        in.writeLine("insert " + TABLE_NAME + " column1 value1 column2 value2");
        in.writeLine("clear " + TABLE_NAME);
        in.writeLine("drop " + TABLE_NAME);
        in.writeLine("exit");

        Main.main(new String[0]);

        String expected = "Hello, type 'help' to get help or 'exit' to quit from program" + System.lineSeparator() +
            "Database has opened successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has created successfully" + System.lineSeparator() +
            "Values to '" + TABLE_NAME + "' table has inserted successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has cleared successfully" + System.lineSeparator() +
            "Table '" + TABLE_NAME + "' has dropped successfully" + System.lineSeparator() +
            "Goodbye, see you later" + System.lineSeparator();
        assertEquals(expected, out.readLine());
    }
}
