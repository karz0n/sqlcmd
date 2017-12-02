package ua.in.denoming.sqlcmd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DataSet.Data;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.command.Find;
import ua.in.denoming.sqlcmd.model.exception.WrongCountOfArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
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
    }

    @Test
    void testWrongCallOfExecute() {
        assertThrows(WrongCountOfArgumentsException.class, command::execute);
    }

    @Test
    void testExecute() {
        ArrayList<DataSet> sets = new ArrayList<>();
        sets.add(new DataSet(
            new Data("name1", "value1"),
            new Data("name2", "value2")
        ));
        when(manager.obtainTableData("test")).thenReturn(sets);

        command.execute("test");

        assertPrint("[+------+------+\n" +
            "|name1 |name2 |\n" +
            "+------+------+\n" +
            "|value1|value2|\n" +
            "+------+------+]");
    }

    private void assertPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).writeLine(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
