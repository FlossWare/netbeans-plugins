package org.flossware.netbeans.gemini.completion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GeminiCompletionSettings
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class GeminiCompletionSettingsTest {

    @Test
    void testIsEnabled_DefaultValue() {
        // Default should be true
        boolean enabled = GeminiCompletionSettings.isEnabled();
        assertThat(enabled).isTrue();
    }

    @Test
    void testIsAutoTriggerEnabled_DefaultValue() {
        // Default should be false
        boolean autoTrigger = GeminiCompletionSettings.isAutoTriggerEnabled();
        assertThat(autoTrigger).isFalse();
    }

    @Test
    void testGetTriggerCharacters_DefaultValue() {
        String triggerChars = GeminiCompletionSettings.getTriggerCharacters();
        assertThat(triggerChars).isEqualTo(".");
    }

    @Test
    void testGetMinimumCharacters_DefaultValue() {
        int minChars = GeminiCompletionSettings.getMinimumCharacters();
        assertThat(minChars).isEqualTo(3);
    }

    @Test
    void testGetDelayMilliseconds_DefaultValue() {
        int delay = GeminiCompletionSettings.getDelayMilliseconds();
        assertThat(delay).isEqualTo(500);
    }

    @Test
    void testIsCacheEnabled_DefaultValue() {
        boolean cacheEnabled = GeminiCompletionSettings.isCacheEnabled();
        assertThat(cacheEnabled).isTrue();
    }

    @Test
    void testGetCacheTTLSeconds_DefaultValue() {
        int cacheTTL = GeminiCompletionSettings.getCacheTTLSeconds();
        assertThat(cacheTTL).isEqualTo(300); // 5 minutes
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> GeminiCompletionSettings.setEnabled(false)).doesNotThrowAnyException();
        assertThatCode(() -> GeminiCompletionSettings.setAutoTriggerEnabled(true)).doesNotThrowAnyException();
        assertThatCode(() -> GeminiCompletionSettings.setTriggerCharacters(".,")).doesNotThrowAnyException();
        assertThatCode(() -> GeminiCompletionSettings.setMinimumCharacters(5)).doesNotThrowAnyException();
        assertThatCode(() -> GeminiCompletionSettings.setDelayMilliseconds(1000)).doesNotThrowAnyException();
        assertThatCode(() -> GeminiCompletionSettings.setCacheEnabled(false)).doesNotThrowAnyException();
        assertThatCode(() -> GeminiCompletionSettings.setCacheTTLSeconds(600)).doesNotThrowAnyException();
    }
}
