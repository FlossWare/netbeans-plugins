/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.swing.text.JTextComponent;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExplainCodeActionTest {

    @Mock
    private JTextComponent mockComponent;

    private ExplainCodeAction action;

    @BeforeEach
    void setUp() {
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        action = new ExplainCodeAction(mockComponent);
    }

    @Test
    void testConstruction() {
        assertThat(action).isNotNull();
    }

    @Test
    void testConstruction_NullComponent() {
        ExplainCodeAction nullAction = new ExplainCodeAction(null);
        assertThat(nullAction.isEnabled()).isFalse();
    }

    @Test
    void testActionPerformed_WithSelection() {
        when(mockComponent.getSelectedText()).thenReturn("code");
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_NoSelection() {
        when(mockComponent.getSelectedText()).thenReturn(null);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_EmptySelection() {
        when(mockComponent.getSelectedText()).thenReturn("   ");
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testIsEnabled_WithSelection() {
        assertThat(action.isEnabled()).isTrue();
    }

    @Test
    void testConstruction_NoSelection() {
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(0);
        ExplainCodeAction noSelAction = new ExplainCodeAction(mockComponent);
        assertThat(noSelAction.isEnabled()).isFalse();
    }

    @Test
    void testGetValue_Name() {
        assertThat(action.getValue("Name")).isNotNull();
    }

    @Test
    void testActionPerformed_LongSelection() {
        String longCode = "code ".repeat(1000);
        when(mockComponent.getSelectedText()).thenReturn(longCode);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }
}
