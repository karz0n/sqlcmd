package ua.in.denoming.sqlcmd.model.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        databaseManager = mock(DatabaseManager.class);
        command = new Connect(view, databaseManager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("someUrl", "someUserName", "somePassword"));

        assertFalse(command.canExecute());
        assertFalse(command.canExecute("too", "many", "arguments", "and", "more"));
    }

    @Test
    void testWrongCallOfExecute() {
        assertThrows(IllegalArgumentException.class, command::execute);
        assertThrows(
            IllegalArgumentException.class,
            () -> command.execute("too", "many", "arguments", "and", "more")
        );
    }

    @Test
    void testExecute() {
        // When
        command.execute("someUrl", "someUserName", "somePassword");

        // Then
        verify(databaseManager, times(1)).open(anyString(), anyString(), anyString());
        verify(view, atLeastOnce()).writeLine(anyString());
    }
}
