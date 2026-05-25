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

package org.flossware.netbeans.claude.completion;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.lookup.ServiceProvider;

/**
 * Provides Claude AI-powered code completions in NetBeans editor
 */
@ServiceProvider(service = CompletionProvider.class)
public class ClaudeCompletionProvider implements CompletionProvider {

    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }

        // Check if completion is enabled
        if (!ClaudeCompletionSettings.isEnabled()) {
            return null;
        }

        // Create async completion task
        return new AsyncCompletionTask(
                new ClaudeCompletionQuery(component),
                component
        );
    }

    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        // Check if we should trigger auto-completion
        if (!ClaudeCompletionSettings.isEnabled()) {
            return 0;
        }

        // Get settings
        boolean autoTrigger = ClaudeCompletionSettings.isAutoTriggerEnabled();
        if (!autoTrigger) {
            return 0; // Only manual Ctrl+Space
        }

        // Trigger on specific characters
        String triggers = ClaudeCompletionSettings.getTriggerCharacters();
        if (typedText != null && !typedText.isEmpty() && triggers.contains(typedText)) {
            return CompletionProvider.COMPLETION_QUERY_TYPE;
        }

        return 0;
    }
}
