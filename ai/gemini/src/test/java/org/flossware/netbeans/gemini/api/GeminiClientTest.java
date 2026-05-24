/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flossware.netbeans.gemini.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GeminiClient
 * Note: Most tests require NetBeans preferences which are not available in unit tests.
 * These tests focus on testing the basic functionality and structure.
 * Full integration tests would require a NetBeans runtime environment.
 */
class GeminiClientTest {

    private GeminiClient client;

    @BeforeEach
    void setUp() {
        client = new GeminiClient();
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
