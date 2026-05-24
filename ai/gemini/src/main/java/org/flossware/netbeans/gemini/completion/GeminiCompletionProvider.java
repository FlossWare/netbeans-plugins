package org.flossware.netbeans.gemini.completion;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.lookup.ServiceProvider;

/**
 * Provides Gemini AI-powered code completions in NetBeans editor
 */
@ServiceProvider(service = CompletionProvider.class)
public class GeminiCompletionProvider implements CompletionProvider {

    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }

        // Check if completion is enabled
        if (!GeminiCompletionSettings.isEnabled()) {
            return null;
        }

        // Create async completion task
        return new AsyncCompletionTask(
                new GeminiCompletionQuery(component),
                component
        );
    }

    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        // Check if we should trigger auto-completion
        if (!GeminiCompletionSettings.isEnabled()) {
            return 0;
        }

        // Get settings
        boolean autoTrigger = GeminiCompletionSettings.isAutoTriggerEnabled();
        if (!autoTrigger) {
            return 0; // Only manual Ctrl+Space
        }

        // Trigger on specific characters
        String triggers = GeminiCompletionSettings.getTriggerCharacters();
        if (typedText != null && triggers.contains(typedText)) {
            return CompletionProvider.COMPLETION_QUERY_TYPE;
        }

        return 0;
    }
}
