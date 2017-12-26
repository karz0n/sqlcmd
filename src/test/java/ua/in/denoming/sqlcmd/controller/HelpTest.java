package ua.in.denoming.sqlcmd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.command.Help;
import ua.in.denoming.sqlcmd.view.View;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HelpTest {
    private View view;
    private Command command;

    @BeforeEach
    void setup() {
        view = mock(View.class);
        command = new Help(view);
    }

    @Test
    void testCanExecute() {
        assertTrue(command.canExecute());
    }
}
