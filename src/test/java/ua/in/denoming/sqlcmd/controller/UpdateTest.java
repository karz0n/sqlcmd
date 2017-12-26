package ua.in.denoming.sqlcmd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.command.Insert;
import ua.in.denoming.sqlcmd.model.command.Update;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
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
