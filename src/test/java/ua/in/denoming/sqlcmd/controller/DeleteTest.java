package ua.in.denoming.sqlcmd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.command.Delete;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

class DeleteTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        databaseManager = mock(DatabaseManager.class);
        command = new Delete(view, databaseManager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("tableName", "column", "searchValue"));

        assertFalse(command.canExecute());
        assertFalse(command.canExecute("too", "many", "arguments", "and", "more"));
    }

    @Test
    void testWrongCallOfExecute() {
        assertThrows(WrongCommandArgumentsException.class, command::execute);
        assertThrows(
            WrongCommandArgumentsException.class,
            () -> command.execute("too", "many", "arguments", "and", "more")
        );
    }

    @Test
    void testConnect() {
        //
        // When
        //
        command.execute("tableName", "column", "searchValue");

        //
        // Then
        //
        verify(databaseManager, times(1)).deleteData(anyString(), anyString(), anyString());
        verify(view, atLeastOnce()).writeFormatLine(anyString(), anyVararg());
    }
}
