package ua.in.denoming.sqlcmd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.command.Clear;
import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClearTest {
    private View view;
    private DatabaseManager manager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        command = new Clear(view, manager);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute("test"));
        assertFalse(command.canExecute());
        assertFalse(
            command.canExecute("too", "many", "arguments")
        );
    }

    @Test
    void testWrongCallOfExecute() {
        assertThrows(WrongCommandArgumentsException.class, command::execute);
        assertThrows(
            WrongCommandArgumentsException.class, () -> command.execute("too", "many", "arguments")
        );
    }

    @Test
    void testExecute() {
        //
        // When
        //
        command.execute("test");

        //
        // Then
        //
        verify(manager, times(1)).clearTable("test");

        String format = "Table '%s' has cleared successfully";
        assertFormatPrint(format, "test");
    }

    private void assertFormatPrint(String format, Object... objects) {
        ArgumentCaptor<String> formatCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> objectsCaptor = ArgumentCaptor.forClass(Object.class);
        verify(view, atLeastOnce()).writeFormatLine(formatCaptor.capture(), objectsCaptor.capture());

        String expected = String.format(format, objects);
        String actual = String.format(
            formatCaptor.getValue(), objectsCaptor.getAllValues().toArray()
        );

        assertEquals(expected, actual);
    }
}
