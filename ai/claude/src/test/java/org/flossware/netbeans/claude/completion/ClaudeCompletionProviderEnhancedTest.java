/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Enhanced tests for ClaudeCompletionProvider to maximize coverage.
 */
class ClaudeCompletionProviderEnhancedTest {

    private ClaudeCompletionProvider provider;
    private boolean originalEnabled;
    private boolean originalAutoTrigger;
    private String originalTriggerChars;

    @BeforeEach
    void setUp() {
        provider = new ClaudeCompletionProvider();

        // Save original settings
        originalEnabled = ClaudeCompletionSettings.isEnabled();
        originalAutoTrigger = ClaudeCompletionSettings.isAutoTriggerEnabled();
        originalTriggerChars = ClaudeCompletionSettings.getTriggerCharacters();
    }

    @AfterEach
    void tearDown() {
        // Restore original settings
        ClaudeCompletionSettings.setEnabled(originalEnabled);
        ClaudeCompletionSettings.setAutoTriggerEnabled(originalAutoTrigger);
        ClaudeCompletionSettings.setTriggerCharacters(originalTriggerChars);
    }

    @Test
    void testCreateTask_CompletionQueryType_Enabled() {
        ClaudeCompletionSettings.setEnabled(true);

        JTextComponent component = mock(JTextComponent.class);
        CompletionTask task = provider.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, component);

        assertThat(task).isNotNull();
    }

    @Test
    void testCreateTask_CompletionQueryType_Disabled() {
        ClaudeCompletionSettings.setEnabled(false);

        JTextComponent component = mock(JTextComponent.class);
        CompletionTask task = provider.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, component);

        assertThat(task).isNull();
    }

    @Test
    void testCreateTask_DocumentationQueryType() {
        ClaudeCompletionSettings.setEnabled(true);

        JTextComponent component = mock(JTextComponent.class);
        CompletionTask task = provider.createTask(CompletionProvider.DOCUMENTATION_QUERY_TYPE, component);

        assertThat(task).isNull();
    }

    @Test
    void testCreateTask_TooltipQueryType() {
        ClaudeCompletionSettings.setEnabled(true);

        JTextComponent component = mock(JTextComponent.class);
        CompletionTask task = provider.createTask(CompletionProvider.TOOLTIP_QUERY_TYPE, component);

        assertThat(task).isNull();
    }

    @Test
    void testCreateTask_AllQueryTypes() {
        ClaudeCompletionSettings.setEnabled(true);

        JTextComponent component = mock(JTextComponent.class);

        for (int queryType = 0; queryType <= 15; queryType++) {
            CompletionTask task = provider.createTask(queryType, component);

            if (queryType == CompletionProvider.COMPLETION_QUERY_TYPE) {
                assertThat(task).isNotNull();
            } else {
                assertThat(task).isNull();
            }
        }
    }

    @Test
    void testGetAutoQueryTypes_Disabled() {
        ClaudeCompletionSettings.setEnabled(false);

        JTextComponent component = mock(JTextComponent.class);
        int types = provider.getAutoQueryTypes(component, ".");

        assertThat(types).isEqualTo(0);
    }

    @Test
    void testGetAutoQueryTypes_EnabledButAutoTriggerDisabled() {
        ClaudeCompletionSettings.setEnabled(true);
        ClaudeCompletionSettings.setAutoTriggerEnabled(false);

        JTextComponent component = mock(JTextComponent.class);
        int types = provider.getAutoQueryTypes(component, ".");

        assertThat(types).isEqualTo(0);
    }

    @Test
    void testGetAutoQueryTypes_EnabledAndAutoTriggerEnabled_MatchingChar() {
        ClaudeCompletionSettings.setEnabled(true);
        ClaudeCompletionSettings.setAutoTriggerEnabled(true);
        ClaudeCompletionSettings.setTriggerCharacters(".");

        JTextComponent component = mock(JTextComponent.class);
        int types = provider.getAutoQueryTypes(component, ".");

        assertThat(types).isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
    }

    @Test
    void testGetAutoQueryTypes_EnabledAndAutoTriggerEnabled_NonMatchingChar() {
        ClaudeCompletionSettings.setEnabled(true);
        ClaudeCompletionSettings.setAutoTriggerEnabled(true);
        ClaudeCompletionSettings.setTriggerCharacters(".");

        JTextComponent component = mock(JTextComponent.class);
        int types = provider.getAutoQueryTypes(component, "a");

        assertThat(types).isEqualTo(0);
    }

    @Test
    void testGetAutoQueryTypes_NullTypedText() {
        ClaudeCompletionSettings.setEnabled(true);
        ClaudeCompletionSettings.setAutoTriggerEnabled(true);
        ClaudeCompletionSettings.setTriggerCharacters(".");

        JTextComponent component = mock(JTextComponent.class);
        int types = provider.getAutoQueryTypes(component, null);

        assertThat(types).isEqualTo(0);
    }

    @Test
    void testGetAutoQueryTypes_EmptyTypedText() {
        ClaudeCompletionSettings.setEnabled(true);
        ClaudeCompletionSettings.setAutoTriggerEnabled(true);
        ClaudeCompletionSettings.setTriggerCharacters(".");

        JTextComponent component = mock(JTextComponent.class);
        int types = provider.getAutoQueryTypes(component, "");

        assertThat(types).isEqualTo(0);
    }

    @Test
    void testGetAutoQueryTypes_MultipleTriggerChars() {
        ClaudeCompletionSettings.setEnabled(true);
        ClaudeCompletionSettings.setAutoTriggerEnabled(true);
        ClaudeCompletionSettings.setTriggerCharacters(".,:");

        JTextComponent component = mock(JTextComponent.class);

        assertThat(provider.getAutoQueryTypes(component, "."))
            .isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
        assertThat(provider.getAutoQueryTypes(component, ","))
            .isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
        assertThat(provider.getAutoQueryTypes(component, ":"))
            .isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
        assertThat(provider.getAutoQueryTypes(component, "a"))
            .isEqualTo(0);
    }

    @Test
    void testGetAutoQueryTypes_SpecialCharacters() {
        ClaudeCompletionSettings.setEnabled(true);
        ClaudeCompletionSettings.setAutoTriggerEnabled(true);
        ClaudeCompletionSettings.setTriggerCharacters("(<[");

        JTextComponent component = mock(JTextComponent.class);

        assertThat(provider.getAutoQueryTypes(component, "("))
            .isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
        assertThat(provider.getAutoQueryTypes(component, "<"))
            .isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
        assertThat(provider.getAutoQueryTypes(component, "["))
            .isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
    }

    @Test
    void testGetAutoQueryTypes_WhitespaceTypedText() {
        ClaudeCompletionSettings.setEnabled(true);
        ClaudeCompletionSettings.setAutoTriggerEnabled(true);
        ClaudeCompletionSettings.setTriggerCharacters(".");

        JTextComponent component = mock(JTextComponent.class);
        int types = provider.getAutoQueryTypes(component, " ");

        assertThat(types).isEqualTo(0);
    }

    @Test
    void testCreateTask_NullComponent() {
        ClaudeCompletionSettings.setEnabled(true);

        CompletionTask task = provider.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, null);

        assertThat(task).isNotNull();
    }

    @Test
    void testGetAutoQueryTypes_NullComponent() {
        ClaudeCompletionSettings.setEnabled(true);
        ClaudeCompletionSettings.setAutoTriggerEnabled(true);
        ClaudeCompletionSettings.setTriggerCharacters(".");

        int types = provider.getAutoQueryTypes(null, ".");

        assertThat(types).isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
    }

    @Test
    void testMultipleInstances() {
        ClaudeCompletionProvider provider1 = new ClaudeCompletionProvider();
        ClaudeCompletionProvider provider2 = new ClaudeCompletionProvider();

        assertThat(provider1).isNotSameAs(provider2);
    }

    @Test
    void testToggleSettings() {
        JTextComponent component = mock(JTextComponent.class);

        // Start disabled
        ClaudeCompletionSettings.setEnabled(false);
        assertThat(provider.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, component)).isNull();

        // Enable
        ClaudeCompletionSettings.setEnabled(true);
        assertThat(provider.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, component)).isNotNull();

        // Disable again
        ClaudeCompletionSettings.setEnabled(false);
        assertThat(provider.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, component)).isNull();
    }
}
