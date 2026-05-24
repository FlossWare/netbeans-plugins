package org.flossware.netbeans.chatgpt.completion;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.lookup.ServiceProvider;

/**
 * Provides ChatGPT AI-powered code completions in NetBeans editor
 */
@ServiceProvider(service = CompletionProvider.class)
public class ChatGPTCompletionProvider implements CompletionProvider {

    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }

        // Check if completion is enabled
        if (!ChatGPTCompletionSettings.isEnabled()) {
            return null;
        }

        // Create async completion task
        return new AsyncCompletionTask(
                new ChatGPTCompletionQuery(component),
                component
        );
    }

    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        // Check if we should trigger auto-completion
        if (!ChatGPTCompletionSettings.isEnabled()) {
            return 0;
        }

        // Get settings
        boolean autoTrigger = ChatGPTCompletionSettings.isAutoTriggerEnabled();
        if (!autoTrigger) {
            return 0; // Only manual Ctrl+Space
        }

        // Trigger on specific characters
        String triggers = ChatGPTCompletionSettings.getTriggerCharacters();
        if (typedText != null && triggers.contains(typedText)) {
            return CompletionProvider.COMPLETION_QUERY_TYPE;
        }

        return 0;
    }
}
