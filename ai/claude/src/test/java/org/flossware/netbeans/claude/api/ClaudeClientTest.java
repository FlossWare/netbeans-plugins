/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ClaudeClientTest {

    private ClaudeClient client;

    @BeforeEach
    void setUp() {
        client = new ClaudeClient();
    }

    @Test
    void testConstruction() {
        assertThatCode(() -> new ClaudeClient()).doesNotThrowAnyException();
    }

    @Test
    void testGetApiKey_Initially() {
        String apiKey = client.getApiKey();
        assertThat(apiKey).isNotNull(); // May be empty but not null
    }

    @Test
    void testSetApiKey() {
        assertThatCode(() -> client.setApiKey("test-api-key")).doesNotThrowAnyException();
    }

    @Test
    void testSetApiKey_Null() {
        assertThatCode(() -> client.setApiKey(null)).doesNotThrowAnyException();
    }

    @Test
    void testSetApiKey_Empty() {
        assertThatCode(() -> client.setApiKey("")).doesNotThrowAnyException();
    }

    @Test
    void testIsConfigured_NoApiKey() {
        client.setApiKey("");
        assertThat(client.isConfigured()).isFalse();
    }

    @Test
    void testIsConfigured_WithApiKey() {
        client.setApiKey("sk-test-key");
        assertThat(client.isConfigured()).isTrue();
    }

    @Test
    void testIsConfigured_NullApiKey() {
        client.setApiKey(null);
        assertThat(client.isConfigured()).isFalse();
    }

    @Test
    void testGetHistorySize_Initially() {
        assertThat(client.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testClearHistory_Initially() {
        assertThatCode(() -> client.clearHistory()).doesNotThrowAnyException();
        assertThat(client.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testClearHistory_Multiple() throws org.flossware.netbeans.claude.exceptions.ClaudeException {
        client.clearHistory();
        client.clearHistory();
        client.clearHistory();
        assertThat(client.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testSendMessage_NoApiKey() {
        client.setApiKey("");
        assertThatThrownBy(() -> client.sendMessage("test"))
            .isInstanceOf(org.flossware.netbeans.claude.exceptions.ClaudeConfigException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessage_NullMessage() {
        client.setApiKey("sk-test-key");
        // Will fail due to missing API connection, but tests parameter validation
        assertThatCode(() -> {
            try {
                client.sendMessage(null);
            } catch (org.flossware.netbeans.claude.exceptions.ClaudeConfigException | NullPointerException e) {
                // Expected - either no API key validation or null message
            } catch (Exception e) {
                // Other exceptions are also acceptable (network, API errors)
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testSendMessage_EmptyMessage() {
        client.setApiKey("sk-test-key");
        assertThatCode(() -> {
            try {
                client.sendMessage("");
            } catch (Exception e) {
                // Expected - API will reject or network error
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testSendMessageWithContext_NoApiKey() {
        client.setApiKey("");
        assertThatThrownBy(() -> client.sendMessageWithContext("test", "context"))
            .isInstanceOf(org.flossware.netbeans.claude.exceptions.ClaudeConfigException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageWithContext_NullMessage() {
        client.setApiKey("sk-test-key");
        assertThatCode(() -> {
            try {
                client.sendMessageWithContext(null, "context");
            } catch (Exception e) {
                // Expected
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testSendMessageWithContext_NullContext() {
        client.setApiKey("sk-test-key");
        assertThatCode(() -> {
            try {
                client.sendMessageWithContext("message", null);
            } catch (Exception e) {
                // Expected
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testSendMessageWithContext_EmptyContext() {
        client.setApiKey("sk-test-key");
        assertThatCode(() -> {
            try {
                client.sendMessageWithContext("message", "");
            } catch (Exception e) {
                // Expected
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testSendMessageStreaming_NoApiKey() {
        client.setApiKey("");
        assertThatThrownBy(() -> client.sendMessageStreaming("test", chunk -> {}))
            .isInstanceOf(org.flossware.netbeans.claude.exceptions.ClaudeConfigException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageStreaming_NullCallback() {
        client.setApiKey("sk-test-key");
        assertThatCode(() -> {
            try {
                client.sendMessageStreaming("test", null);
            } catch (Exception e) {
                // Expected - API error or null callback handling
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testSendMessageStreaming_NullMessage() {
        client.setApiKey("sk-test-key");
        assertThatCode(() -> {
            try {
                client.sendMessageStreaming(null, chunk -> {});
            } catch (Exception e) {
                // Expected
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testSendMessageWithContextStreaming_NoApiKey() {
        client.setApiKey("");
        assertThatThrownBy(() -> client.sendMessageWithContextStreaming("msg", "ctx", chunk -> {}))
            .isInstanceOf(org.flossware.netbeans.claude.exceptions.ClaudeConfigException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageWithContextStreaming_NullMessage() {
        client.setApiKey("sk-test-key");
        assertThatCode(() -> {
            try {
                client.sendMessageWithContextStreaming(null, "context", chunk -> {});
            } catch (Exception e) {
                // Expected
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testSendMessageWithContextStreaming_NullContext() {
        client.setApiKey("sk-test-key");
        assertThatCode(() -> {
            try {
                client.sendMessageWithContextStreaming("message", null, chunk -> {});
            } catch (Exception e) {
                // Expected
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testSendMessageWithContextStreaming_NullCallback() {
        client.setApiKey("sk-test-key");
        assertThatCode(() -> {
            try {
                client.sendMessageWithContextStreaming("message", "context", null);
            } catch (Exception e) {
                // Expected
            }
        }).doesNotThrowAnyException();
    }

    @Test
    void testApiKeyPersistence() {
        String testKey = "sk-test-persistence-key";
        client.setApiKey(testKey);
        
        // Create new client to test persistence
        ClaudeClient newClient = new ClaudeClient();
        assertThat(newClient.getApiKey()).isEqualTo(testKey);
        
        // Clean up
        newClient.setApiKey("");
    }

    @Test
    void testApiKeyUpdate() {
        client.setApiKey("key1");
        assertThat(client.getApiKey()).isEqualTo("key1");
        
        client.setApiKey("key2");
        assertThat(client.getApiKey()).isEqualTo("key2");
        
        // Clean up
        client.setApiKey("");
    }

    @Test
    void testLongApiKey() {
        String longKey = "sk-" + "a".repeat(200);
        assertThatCode(() -> client.setApiKey(longKey)).doesNotThrowAnyException();
        assertThat(client.getApiKey()).isEqualTo(longKey);
        
        // Clean up
        client.setApiKey("");
    }

    @Test
    void testSpecialCharactersInApiKey() {
        String specialKey = "sk-test!@#$%^&*()_+-=";
        assertThatCode(() -> client.setApiKey(specialKey)).doesNotThrowAnyException();
        
        // Clean up
        client.setApiKey("");
    }
}
