package ua.in.denoming.sqlcmd.model.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        databaseManager = mock(DatabaseManager.class);
        command = new Update(view, databaseManager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("tableName", "column", "searchValue", "value"));

        assertFalse(command.canExecute());
        assertFalse(command.canExecute("bsolutely", "wrong", "count", "of", "arguments"));
    }

    @Test
    void testWrongCallOfExecute() {
        assertThrows(WrongCommandArgumentsException.class, command::execute);
        assertThrows(
            WrongCommandArgumentsException.class,
            () -> command.execute("absolutely", "wrong", "count", "of", "arguments")
        );
    }

    @Test
    void testExecute() {
        //
        // When
        //
        command.execute("tableName", "column", "searchValue", "value");

        //
        // Then
        //
        verify(databaseManager, times(1)).updateData(
            anyString(), anyString(), anyString(), anyString()
        );
        verify(view, atLeastOnce()).writeFormatLine(anyString(), anyVararg());
    }
}
