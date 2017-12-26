package ua.in.denoming.sqlcmd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.command.Drop;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

class DropTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;


    @BeforeEach
    void setup() {
        view = mock(View.class);
        databaseManager = mock(DatabaseManager.class);
        command = new Drop(view, databaseManager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("tableName"));

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
        command.execute("tableName");

        //
        // Then
        //
        verify(databaseManager, times(1)).deleteTable(anyString());
        verify(view, atLeastOnce()).writeFormatLine(anyString(), anyVararg());
    }
}
