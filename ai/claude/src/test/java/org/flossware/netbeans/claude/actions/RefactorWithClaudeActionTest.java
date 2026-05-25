/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.actions;

import javax.swing.text.JTextComponent;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefactorWithClaudeActionTest {

    @Test
    void testConstruction_ValidComponent() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        
        assertThatCode(() -> new RefactorWithClaudeAction(mockComponent)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_NullComponent() {
        assertThatCode(() -> new RefactorWithClaudeAction(null)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_NoSelection() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(5);
        when(mockComponent.getSelectionEnd()).thenReturn(5); // Same position = no selection
        
        RefactorWithClaudeAction action = new RefactorWithClaudeAction(mockComponent);
        assertThat(action.isEnabled()).isFalse();
    }

    @Test
    void testConstruction_WithSelection() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        
        RefactorWithClaudeAction action = new RefactorWithClaudeAction(mockComponent);
        assertThat(action.isEnabled()).isTrue();
    }

    @Test
    void testActionPerformed_NullSelectedText() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        when(mockComponent.getSelectedText()).thenReturn(null);
        
        RefactorWithClaudeAction action = new RefactorWithClaudeAction(mockComponent);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_EmptySelectedText() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        when(mockComponent.getSelectedText()).thenReturn("");
        
        RefactorWithClaudeAction action = new RefactorWithClaudeAction(mockComponent);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_WhitespaceSelectedText() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        when(mockComponent.getSelectedText()).thenReturn("   \n\t  ");
        
        RefactorWithClaudeAction action = new RefactorWithClaudeAction(mockComponent);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_ValidSelectedText() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        when(mockComponent.getSelectedText()).thenReturn("public void test(){}");
        
        RefactorWithClaudeAction action = new RefactorWithClaudeAction(mockComponent);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }
}
