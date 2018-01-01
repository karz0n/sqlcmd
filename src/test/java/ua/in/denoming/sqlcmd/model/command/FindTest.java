package ua.in.denoming.sqlcmd.model.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.WrongArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        databaseManager = mock(DatabaseManager.class);
        command = new Find(view, databaseManager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("tableName"));
        assertFalse(command.canExecute());
        assertFalse(
            command.canExecute("too", "many", "arguments")
        );
    }

    @Test
    void testWrongCallOfExecute() {
        assertThrows(WrongArgumentsException.class, command::execute);
        assertThrows(
            WrongArgumentsException.class, () -> command.execute("too", "many", "arguments")
        );
    }

    @Test
    void testExecute() {
        //
        // When
        //
        command.execute("tableName");

        //
        // Then
        //
        verify(databaseManager, times(1)).getData(anyString());
        verify(view, atLeastOnce()).writeLine(anyString());
    }
}
