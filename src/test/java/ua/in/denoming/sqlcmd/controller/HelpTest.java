package ua.in.denoming.sqlcmd.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.command.Help;
import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HelpTest {
    private View view = Mockito.mock(View.class);

    @Test
    void testCanExecute() {
        Command command = new Help(view);
        assertTrue(command.canExecute());
    }
}
