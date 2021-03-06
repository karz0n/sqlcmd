package ua.in.denoming.sqlcmd.model.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TablesTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        databaseManager = mock(DatabaseManager.class);
        command = new Tables(view, databaseManager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("some", "string"));
    }

    @Test
    void testExecute() {
        //
        // When
        //
        command.execute("some", "string");

        //
        // Then
        //
        verify(databaseManager, times(1)).getTables();
        verify(view, atLeastOnce()).writeLine(anyString());
    }
}
