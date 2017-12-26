package ua.in.denoming.sqlcmd.model.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        databaseManager = mock(DatabaseManager.class);
        command = new Create(view, databaseManager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("table", "column1", "column2"));

        assertFalse(command.canExecute());
        assertFalse(command.canExecute("wrong"));
    }

    @Test
    void testWrongCallOfExecute() {
        assertThrows(WrongCommandArgumentsException.class, command::execute);
        assertThrows(
            WrongCommandArgumentsException.class,
            () -> command.execute("wrong")
        );
    }

    @Test
    void testConnect() {
        //
        // When
        //
        command.execute("table", "column1", "column2");

        //
        // Then
        //
        verify(databaseManager, times(1)).createTable(anyString(), anyVararg());
        verify(view, atLeastOnce()).writeFormatLine(anyString(), anyVararg());
    }
}
