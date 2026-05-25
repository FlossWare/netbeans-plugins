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

package org.flossware.netbeans.chatgpt.completion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for ChatGPTCompletionSettings
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class ChatGPTCompletionSettingsTest {

    @Test
    void testIsEnabled_ReturnsValue() {
        // May return different values based on preferences
        assertThatCode(() -> ChatGPTCompletionSettings.isEnabled()).doesNotThrowAnyException();
    }

    @Test
    void testIsAutoTriggerEnabled_ReturnsValue() {
        assertThatCode(() -> ChatGPTCompletionSettings.isAutoTriggerEnabled()).doesNotThrowAnyException();
    }

    @Test
    void testGetTriggerCharacters_ReturnsValue() {
        String triggerChars = ChatGPTCompletionSettings.getTriggerCharacters();
        assertThat(triggerChars).isNotNull();
    }

    @Test
    void testGetMinimumCharacters_ReturnsValue() {
        int minChars = ChatGPTCompletionSettings.getMinimumCharacters();
        assertThat(minChars).isGreaterThan(0);
    }

    @Test
    void testGetDelayMilliseconds_ReturnsValue() {
        int delay = ChatGPTCompletionSettings.getDelayMilliseconds();
        assertThat(delay).isGreaterThan(0);
    }

    @Test
    void testIsCacheEnabled_ReturnsValue() {
        assertThatCode(() -> ChatGPTCompletionSettings.isCacheEnabled()).doesNotThrowAnyException();
    }

    @Test
    void testGetCacheTTLSeconds_ReturnsValue() {
        int cacheTTL = ChatGPTCompletionSettings.getCacheTTLSeconds();
        assertThat(cacheTTL).isGreaterThan(0);
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> ChatGPTCompletionSettings.setEnabled(false)).doesNotThrowAnyException();
        assertThatCode(() -> ChatGPTCompletionSettings.setAutoTriggerEnabled(true)).doesNotThrowAnyException();
        assertThatCode(() -> ChatGPTCompletionSettings.setTriggerCharacters(".,")).doesNotThrowAnyException();
        assertThatCode(() -> ChatGPTCompletionSettings.setMinimumCharacters(5)).doesNotThrowAnyException();
        assertThatCode(() -> ChatGPTCompletionSettings.setDelayMilliseconds(1000)).doesNotThrowAnyException();
        assertThatCode(() -> ChatGPTCompletionSettings.setCacheEnabled(false)).doesNotThrowAnyException();
        assertThatCode(() -> ChatGPTCompletionSettings.setCacheTTLSeconds(600)).doesNotThrowAnyException();
    }
}
