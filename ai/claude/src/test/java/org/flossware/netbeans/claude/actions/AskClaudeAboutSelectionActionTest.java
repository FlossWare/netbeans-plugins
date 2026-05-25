/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.actions;

import javax.swing.text.JTextComponent;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AskClaudeAboutSelectionActionTest {

    @Test
    void testConstruction_ValidComponent() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        
        assertThatCode(() -> new AskClaudeAboutSelectionAction(mockComponent)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_NullComponent() {
        assertThatCode(() -> new AskClaudeAboutSelectionAction(null)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_NoSelection() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(5);
        when(mockComponent.getSelectionEnd()).thenReturn(5);
        
        AskClaudeAboutSelectionAction action = new AskClaudeAboutSelectionAction(mockComponent);
        assertThat(action.isEnabled()).isFalse();
    }

    @Test
    void testConstruction_WithSelection() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        
        AskClaudeAboutSelectionAction action = new AskClaudeAboutSelectionAction(mockComponent);
        assertThat(action.isEnabled()).isTrue();
    }

    @Test
    void testActionPerformed_NullSelectedText() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        when(mockComponent.getSelectedText()).thenReturn(null);
        
        AskClaudeAboutSelectionAction action = new AskClaudeAboutSelectionAction(mockComponent);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_EmptySelectedText() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        when(mockComponent.getSelectedText()).thenReturn("");
        
        AskClaudeAboutSelectionAction action = new AskClaudeAboutSelectionAction(mockComponent);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_WhitespaceSelectedText() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        when(mockComponent.getSelectedText()).thenReturn("   \n\t  ");
        
        AskClaudeAboutSelectionAction action = new AskClaudeAboutSelectionAction(mockComponent);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_ValidSelectedText() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        when(mockComponent.getSelectedText()).thenReturn("System.out.println();");
        
        AskClaudeAboutSelectionAction action = new AskClaudeAboutSelectionAction(mockComponent);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }
}
