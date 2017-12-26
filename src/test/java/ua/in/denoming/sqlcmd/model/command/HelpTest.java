package ua.in.denoming.sqlcmd.model.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.in.denoming.sqlcmd.view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
