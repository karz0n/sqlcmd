package ua.in.denoming.sqlcmd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DataSet.Data;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.command.Find;
import ua.in.denoming.sqlcmd.model.exception.WrongCountOfArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import java.util.ArrayList;

class FindTest {
    private View view;
    private DatabaseManager manager;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
    }

    @Test
    void testCanExecute() {
        Command command = new Find(view, manager);
        assertTrue(command.canExecute("test"));
        assertFalse(command.canExecute());
    }

    @Test
    void testWrongCallOfExecute() {
        Command command = new Find(view, manager);
        assertThrows(WrongCountOfArgumentsException.class, command::execute);
    }

    @Test
    void testExecute() {
        Command command = new Find(view, manager);

        ArrayList<DataSet> sets = new ArrayList<>();
        sets.add(new DataSet(
            new Data("name1", "value1"),
            new Data("name2", "value2")
        ));
        when(manager.obtainTableData("test")).thenReturn(sets);

        command.execute("test");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).writeLine(captor.capture());
        assertEquals("[+------+------+\n" +
            "|name1 |name2 |\n" +
            "+------+------+\n" +
            "|value1|value2|\n" +
            "+------+------+]", captor.getAllValues().toString());
    }
}
