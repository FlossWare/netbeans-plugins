package org.flossware.netbeans.claude.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for ClaudeClient
 * Note: Most tests require NetBeans preferences which are not available in unit tests.
 * These tests focus on testing the basic functionality and structure.
 * Full integration tests would require a NetBeans runtime environment.
 */
class ClaudeClientTest {

    private ClaudeClient client;

    @BeforeEach
    void setUp() {
        client = new ClaudeClient();
    }

    @Test
    void testClientInstantiation() {
        assertThat(client).isNotNull();
    }

    @Test
    void testIsConfigured_DefaultsToFalse() {
        // Without NetBeans preferences, should return false
        assertThat(client.isConfigured()).isFalse();
    }

    @Test
    void testGetHistorySize_InitiallyZero() {
        assertThat(client.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testClearHistory_DoesNotThrow() {
        assertThatCode(() -> client.clearHistory()).doesNotThrowAnyException();
        assertThat(client.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testSendMessage_ThrowsWhenNotConfigured() {
        assertThatThrownBy(() -> client.sendMessage("Hello"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageWithContext_ThrowsWhenNotConfigured() {
        assertThatThrownBy(() -> client.sendMessageWithContext("Question", "Context"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageStreaming_ThrowsWhenNotConfigured() {
        assertThatThrownBy(() -> client.sendMessageStreaming("Hello", chunk -> {}))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageWithContextStreaming_ThrowsWhenNotConfigured() {
        assertThatThrownBy(() -> client.sendMessageWithContextStreaming("Question", "Context", chunk -> {}))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("API key not configured");
    }
}
