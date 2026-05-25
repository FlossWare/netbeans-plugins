/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.ui;

import java.awt.Color;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ChatMessagePanelTest {

    @Test
    void testConstruction() {
        assertThatCode(() -> new ChatMessagePanel("sender", "message", Color.BLACK))
            .doesNotThrowAnyException();
    }

    @Test
    void testConstruction_EmptyMessage() {
        assertThatCode(() -> new ChatMessagePanel("sender", "", Color.BLACK))
            .doesNotThrowAnyException();
    }

    @Test
    void testConstruction_LongMessage() {
        String longMessage = "a".repeat(10000);
        assertThatCode(() -> new ChatMessagePanel("sender", longMessage, Color.BLACK))
            .doesNotThrowAnyException();
    }

    @Test
    void testConstruction_MessageWithNewlines() {
        assertThatCode(() -> new ChatMessagePanel("sender", "line1\nline2\nline3", Color.BLACK))
            .doesNotThrowAnyException();
    }

    @Test
    void testConstruction_SpecialCharacters() {
        assertThatCode(() -> new ChatMessagePanel("sender", "<html>&test;</html>", Color.BLACK))
            .doesNotThrowAnyException();
    }

    @Test
    void testConstruction_DifferentColors() {
        assertThatCode(() -> new ChatMessagePanel("sender", "message", Color.RED))
            .doesNotThrowAnyException();
        assertThatCode(() -> new ChatMessagePanel("sender", "message", Color.BLUE))
            .doesNotThrowAnyException();
    }
}
