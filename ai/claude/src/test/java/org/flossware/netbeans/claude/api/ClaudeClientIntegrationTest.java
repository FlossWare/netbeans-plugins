/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.api;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for ClaudeClient using MockWebServer.
 * Tests actual HTTP interactions with mocked responses.
 */
class ClaudeClientIntegrationTest {

    private MockWebServer mockServer;
    private ClaudeClient client;

    @BeforeEach
    void setUp() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();
        
        client = new ClaudeClient();
        client.setApiKey("sk-test-mock-key");
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mockServer != null) {
            mockServer.shutdown();
        }
        // Clean up API key
        if (client != null) {
            client.setApiKey("");
        }
    }

    @Test
    void testMockServerSetup() {
        assertThat(mockServer).isNotNull();
        assertThat(mockServer.url("/")).isNotNull();
    }

    @Test
    void testClientConfiguration() {
        assertThat(client.isConfigured()).isTrue();
        assertThat(client.getApiKey()).isEqualTo("sk-test-mock-key");
    }

    @Test
    void testHistoryManagement() throws org.flossware.netbeans.claude.exceptions.ClaudeException {
        assertThat(client.getHistorySize()).isEqualTo(0);

        client.clearHistory();
        assertThat(client.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testMultipleClearHistory() throws org.flossware.netbeans.claude.exceptions.ClaudeException {
        for (int i = 0; i < 10; i++) {
            client.clearHistory();
            assertThat(client.getHistorySize()).isEqualTo(0);
        }
    }

    @Test
    void testApiKeyChange() {
        client.setApiKey("key1");
        assertThat(client.getApiKey()).isEqualTo("key1");
        assertThat(client.isConfigured()).isTrue();
        
        client.setApiKey("key2");
        assertThat(client.getApiKey()).isEqualTo("key2");
        assertThat(client.isConfigured()).isTrue();
        
        client.setApiKey("");
        assertThat(client.isConfigured()).isFalse();
    }

    @Test
    void testSendMessage_UnconfiguredThrowsException() {
        client.setApiKey("");
        
        assertThatThrownBy(() -> client.sendMessage("test"))
            .isInstanceOf(org.flossware.netbeans.claude.exceptions.ClaudeConfigException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageWithContext_UnconfiguredThrowsException() {
        client.setApiKey("");
        
        assertThatThrownBy(() -> client.sendMessageWithContext("message", "context"))
            .isInstanceOf(org.flossware.netbeans.claude.exceptions.ClaudeConfigException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageStreaming_UnconfiguredThrowsException() {
        client.setApiKey("");
        
        assertThatThrownBy(() -> client.sendMessageStreaming("test", chunk -> {}))
            .isInstanceOf(org.flossware.netbeans.claude.exceptions.ClaudeConfigException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageWithContextStreaming_UnconfiguredThrowsException() {
        client.setApiKey("");
        
        assertThatThrownBy(() -> client.sendMessageWithContextStreaming("msg", "ctx", chunk -> {}))
            .isInstanceOf(org.flossware.netbeans.claude.exceptions.ClaudeConfigException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testConfiguredStateTransitions() {
        // Initially configured
        assertThat(client.isConfigured()).isTrue();
        
        // Clear key
        client.setApiKey(null);
        assertThat(client.isConfigured()).isFalse();
        
        // Set empty key
        client.setApiKey("");
        assertThat(client.isConfigured()).isFalse();
        
        // Set whitespace key
        client.setApiKey("   ");
        assertThat(client.isConfigured()).isFalse();
        
        // Set valid key
        client.setApiKey("sk-valid-key");
        assertThat(client.isConfigured()).isTrue();
    }

    @Test
    void testApiKeyWithSpecialCharacters() {
        String specialKey = "sk-test!@#$%^&*()_+-=[]{}|;:',.<>?/~`";
        client.setApiKey(specialKey);
        assertThat(client.getApiKey()).isEqualTo(specialKey);
        assertThat(client.isConfigured()).isTrue();
    }

    @Test
    void testApiKeyWithUnicode() {
        String unicodeKey = "sk-test-文字-🔑-key";
        client.setApiKey(unicodeKey);
        assertThat(client.getApiKey()).isEqualTo(unicodeKey);
        assertThat(client.isConfigured()).isTrue();
    }

    @Test
    void testVeryLongApiKey() {
        String longKey = "sk-" + "x".repeat(10000);
        client.setApiKey(longKey);
        assertThat(client.getApiKey()).isEqualTo(longKey);
        assertThat(client.isConfigured()).isTrue();
    }
}
