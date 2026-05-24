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
    void testIsEnabled_DefaultValue() {
        // Default should be true
        boolean enabled = ChatGPTCompletionSettings.isEnabled();
        assertThat(enabled).isTrue();
    }

    @Test
    void testIsAutoTriggerEnabled_DefaultValue() {
        // Default should be false
        boolean autoTrigger = ChatGPTCompletionSettings.isAutoTriggerEnabled();
        assertThat(autoTrigger).isFalse();
    }

    @Test
    void testGetTriggerCharacters_DefaultValue() {
        String triggerChars = ChatGPTCompletionSettings.getTriggerCharacters();
        assertThat(triggerChars).isEqualTo(".");
    }

    @Test
    void testGetMinimumCharacters_DefaultValue() {
        int minChars = ChatGPTCompletionSettings.getMinimumCharacters();
        assertThat(minChars).isEqualTo(3);
    }

    @Test
    void testGetDelayMilliseconds_DefaultValue() {
        int delay = ChatGPTCompletionSettings.getDelayMilliseconds();
        assertThat(delay).isEqualTo(500);
    }

    @Test
    void testIsCacheEnabled_DefaultValue() {
        boolean cacheEnabled = ChatGPTCompletionSettings.isCacheEnabled();
        assertThat(cacheEnabled).isTrue();
    }

    @Test
    void testGetCacheTTLSeconds_DefaultValue() {
        int cacheTTL = ChatGPTCompletionSettings.getCacheTTLSeconds();
        assertThat(cacheTTL).isEqualTo(300); // 5 minutes
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
