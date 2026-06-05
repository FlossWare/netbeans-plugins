/*
 * Copyright 2026 FlossWare.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.flossware.netbeans.grok.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GrokClient.
 * Note: These tests verify the client API but don't make actual HTTP calls.
 */
class GrokClientTest {

    private GrokClient client;

    @BeforeEach
    void setUp() {
        client = new GrokClient();
    }

    @Test
    void testClientConstruction() {
        assertThat(client).isNotNull();
    }

    @Test
    void testIsConfigured_WithoutApiKey() {
        // Without API key configured, should return false
        assertThat(client.isConfigured()).isFalse();
    }

    @Test
    void testGetHistorySize_Initially() {
        // Initially conversation history should be empty
        assertThat(client.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testClearHistory_DoesNotThrow() {
        assertThatCode(() -> client.clearHistory()).doesNotThrowAnyException();
    }

    @Test
    void testGetHistorySize_AfterClear() {
        client.clearHistory();
        assertThat(client.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testSendMessage_WithoutApiKey_ThrowsException() {
        // Without API key configured, should throw exception
        assertThatThrownBy(() -> client.sendMessage("Hello"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageWithContext_WithoutApiKey_ThrowsException() {
        // Without API key configured, should throw exception
        assertThatThrownBy(() -> client.sendMessageWithContext("Question", "Context"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageStreaming_WithoutApiKey_ThrowsException() {
        // Without API key configured, should throw exception
        assertThatThrownBy(() -> client.sendMessageStreaming("Hello", chunk -> {}))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSendMessageWithContextStreaming_WithoutApiKey_ThrowsException() {
        // Without API key configured, should throw exception
        assertThatThrownBy(() -> client.sendMessageWithContextStreaming("Question", "Context", chunk -> {}))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("API key not configured");
    }

    @Test
    void testSetModel_DoesNotThrow() {
        assertThatCode(() -> client.setModel("grok-2")).doesNotThrowAnyException();
    }

    @Test
    void testSetMaxTokens_DoesNotThrow() {
        assertThatCode(() -> client.setMaxTokens(8192)).doesNotThrowAnyException();
    }

    @Test
    void testSetTemperature_DoesNotThrow() {
        assertThatCode(() -> client.setTemperature(0.5)).doesNotThrowAnyException();
    }

    @Test
    void testGetConversationHistory_InitiallyEmpty() {
        assertThat(client.getConversationHistory()).isEmpty();
    }

    @Test
    void testGetConversationHistory_ReturnsCopy() {
        var history1 = client.getConversationHistory();
        var history2 = client.getConversationHistory();
        assertThat(history1).isNotSameAs(history2);
    }

    @Test
    void testClearHistory_ResetsSize() {
        client.clearHistory();
        assertThat(client.getHistorySize()).isEqualTo(0);
        assertThat(client.getConversationHistory()).isEmpty();
    }

    @Test
    void testSetApiKey_EmptyString() {
        client.setApiKey("");
        assertThat(client.isConfigured()).isFalse();
    }

    @Test
    void testSendMessageAsync_WithoutApiKey_CompletesExceptionally() {
        var future = client.sendMessageAsync("Hello");
        assertThatThrownBy(() -> future.get(5, java.util.concurrent.TimeUnit.SECONDS))
            .hasCauseInstanceOf(IllegalStateException.class);
    }

    @Test
    void testSendMessageWithContextAsync_WithoutApiKey_CompletesExceptionally() {
        var future = client.sendMessageWithContextAsync("Question", "Context");
        assertThatThrownBy(() -> future.get(5, java.util.concurrent.TimeUnit.SECONDS))
            .hasCauseInstanceOf(IllegalStateException.class);
    }
}
