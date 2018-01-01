package ua.in.denoming.sqlcmd.model.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.WrongArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsertTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        databaseManager = mock(DatabaseManager.class);
        command = new Insert(view, databaseManager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("tableName", "name", "value"));

        assertFalse(command.canExecute());
        assertFalse(command.canExecute("wrong", "count", "of", "arguments"));
    }

    @Test
    void testWrongCallOfExecute() {
        assertThrows(WrongArgumentsException.class, command::execute);
        assertThrows(
            WrongArgumentsException.class,
            () -> command.execute("wrong", "count", "of", "arguments")
        );
    }

    @Test
    void testExecute() {
        //
        // When
        //
        command.execute("tableName", "name", "value");

        //
        // Then
        //
        verify(databaseManager, times(1)).insertData(anyString(), any(DataSet.class));
        verify(view, atLeastOnce()).writeFormatLine(anyString(), anyVararg());
    }
}
