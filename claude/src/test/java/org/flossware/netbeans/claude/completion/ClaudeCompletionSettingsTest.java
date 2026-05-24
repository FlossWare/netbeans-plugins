package org.flossware.netbeans.claude.completion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for ClaudeCompletionSettings
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class ClaudeCompletionSettingsTest {

    @Test
    void testIsEnabled_DefaultValue() {
        // Default should be true
        boolean enabled = ClaudeCompletionSettings.isEnabled();
        assertThat(enabled).isTrue();
    }

    @Test
    void testIsAutoTriggerEnabled_DefaultValue() {
        // Default should be false
        boolean autoTrigger = ClaudeCompletionSettings.isAutoTriggerEnabled();
        assertThat(autoTrigger).isFalse();
    }

    @Test
    void testGetTriggerCharacters_DefaultValue() {
        String triggerChars = ClaudeCompletionSettings.getTriggerCharacters();
        assertThat(triggerChars).isEqualTo(".");
    }

    @Test
    void testGetMinimumCharacters_DefaultValue() {
        int minChars = ClaudeCompletionSettings.getMinimumCharacters();
        assertThat(minChars).isEqualTo(3);
    }

    @Test
    void testGetDelayMilliseconds_DefaultValue() {
        int delay = ClaudeCompletionSettings.getDelayMilliseconds();
        assertThat(delay).isEqualTo(500);
    }

    @Test
    void testIsCacheEnabled_DefaultValue() {
        boolean cacheEnabled = ClaudeCompletionSettings.isCacheEnabled();
        assertThat(cacheEnabled).isTrue();
    }

    @Test
    void testGetCacheTTLSeconds_DefaultValue() {
        int cacheTTL = ClaudeCompletionSettings.getCacheTTLSeconds();
        assertThat(cacheTTL).isEqualTo(300); // 5 minutes
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> ClaudeCompletionSettings.setEnabled(false)).doesNotThrowAnyException();
        assertThatCode(() -> ClaudeCompletionSettings.setAutoTriggerEnabled(true)).doesNotThrowAnyException();
        assertThatCode(() -> ClaudeCompletionSettings.setTriggerCharacters(".,")).doesNotThrowAnyException();
        assertThatCode(() -> ClaudeCompletionSettings.setMinimumCharacters(5)).doesNotThrowAnyException();
        assertThatCode(() -> ClaudeCompletionSettings.setDelayMilliseconds(1000)).doesNotThrowAnyException();
        assertThatCode(() -> ClaudeCompletionSettings.setCacheEnabled(false)).doesNotThrowAnyException();
        assertThatCode(() -> ClaudeCompletionSettings.setCacheTTLSeconds(600)).doesNotThrowAnyException();
    }
}
