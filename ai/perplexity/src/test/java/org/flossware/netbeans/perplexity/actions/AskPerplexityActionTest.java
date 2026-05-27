/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.perplexity.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.swing.text.JTextComponent;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AskPerplexityActionTest {

    @Mock
    private JTextComponent mockComponent;

    private AskPerplexityAction action;

    @BeforeEach
    void setUp() {
        action = new AskPerplexityAction(mockComponent);
    }

    @Test
    void testConstruction() {
        assertThat(action).isNotNull();
    }

    @Test
    void testConstruction_NullComponent() {
        AskPerplexityAction nullAction = new AskPerplexityAction(null);
        assertThat(nullAction).isNotNull();
    }

    @Test
    void testActionPerformed_WithSelection() {
        when(mockComponent.getSelectedText()).thenReturn("public class Test {}");
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
    void testActionPerformed_LongSelection() {
        String longCode = "public class Test { ".repeat(100) + "}";
        when(mockComponent.getSelectedText()).thenReturn(longCode);
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }
}
