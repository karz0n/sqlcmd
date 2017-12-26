package ua.in.denoming.sqlcmd.model.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClearTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        databaseManager = mock(DatabaseManager.class);
        command = new Clear(view, databaseManager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("someName"));

        assertFalse(command.canExecute());
        assertFalse(command.canExecute("too", "many", "arguments"));
    }

    @Test
    void testWrongCallOfExecute() {
        assertThrows(WrongCommandArgumentsException.class, command::execute);
        assertThrows(
            WrongCommandArgumentsException.class,
            () -> command.execute("too", "many", "arguments")
        );
    }

    @Test
    void testExecute() {
        //
        // When
        //
        command.execute("someName");

        //
        // Then
        //
        verify(databaseManager, times(1)).clearTable(anyString());
        verify(view, atLeastOnce()).writeFormatLine(anyString(), anyVararg());
    }

}
