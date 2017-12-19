package ua.in.denoming.sqlcmd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DataSet.Data;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.command.Find;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class FindTest {
    private View view;
    private DatabaseManager manager;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        command = new Find(view, manager);
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
        // Given
        //
        ArrayList<DataSet> sets = new ArrayList<>();
        sets.add(new DataSet(
            new Data("name1", "value1"),
            new Data("name2", "value2")
        ));
        when(manager.obtainTableData("test")).thenReturn(sets);

        //
        // When
        //
        command.execute("test");

        //
        // Then
        //
        String expected =
            "+----------+----------+" + System.lineSeparator() +
            "|  name1   |  name2   |" + System.lineSeparator() +
            "+----------+----------+" + System.lineSeparator() +
            "|  value1  |  value2  |" + System.lineSeparator() +
            "+----------+----------+";
        assertPrint(expected);
    }

    private void assertPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).writeLine(captor.capture());
        assertEquals(expected, captor.getValue());
    }
}
