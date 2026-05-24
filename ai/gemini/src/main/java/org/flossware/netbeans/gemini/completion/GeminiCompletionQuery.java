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

package org.flossware.netbeans.gemini.completion;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.gemini.api.GeminiService;
import org.flossware.netbeans.gemini.completion.CompletionContextBuilder.CompletionContext;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.openide.util.Exceptions;

/**
 * Async query for Gemini code completions
 */
public class GeminiCompletionQuery extends AsyncCompletionQuery {

    private final JTextComponent component;

    public GeminiCompletionQuery(JTextComponent component) {
        this.component = component;
    }

    @Override
    protected void query(CompletionResultSet resultSet, javax.swing.text.Document doc, int caretOffset) {
        try {
            // Build context from editor
            CompletionContextBuilder builder = new CompletionContextBuilder(component, caretOffset);
            CompletionContext context = builder.build();

            // Check minimum characters
            if (context.getPrefix().length() < GeminiCompletionSettings.getMinimumCharacters()) {
                resultSet.finish();
                return;
            }

            // Check cache first
            CompletionCache cache = CompletionCache.getInstance();
            String cacheKey = context.getTextBefore() + "|" + context.getPrefix();

            if (GeminiCompletionSettings.isCacheEnabled()) {
                List<GeminiCompletionItem> cached = cache.get(cacheKey);
                if (cached != null) {
                    cached.forEach(resultSet::addItem);
                    resultSet.finish();
                    return;
                }
            }

            // Query Gemini
            String prompt = context.buildPrompt();
            GeminiService service = GeminiService.getInstance();

            if (!service.isConfigured()) {
                resultSet.finish();
                return;
            }

            // Get completions from Gemini
            service.sendMessageAsync(prompt).thenAccept(response -> {
                List<GeminiCompletionItem> items = parseCompletions(response, context);

                // Add to cache
                if (GeminiCompletionSettings.isCacheEnabled()) {
                    cache.put(cacheKey, items);
                }

                // Add to result set
                items.forEach(resultSet::addItem);
                resultSet.finish();
            }).exceptionally(ex -> {
                Exceptions.printStackTrace(ex);
                resultSet.finish();
                return null;
            });

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            resultSet.finish();
        }
    }

    /**
     * Parse Gemini's response into completion items
     */
    private List<GeminiCompletionItem> parseCompletions(String response, CompletionContext context) {
        List<GeminiCompletionItem> items = new ArrayList<>();

        if (response == null || response.trim().isEmpty()) {
            return items;
        }

        // Clean up response (remove markdown code blocks if present)
        String cleaned = response.trim();
        if (cleaned.startsWith("```")) {
            int start = cleaned.indexOf('\n');
            int end = cleaned.lastIndexOf("```");
            if (start > 0 && end > start) {
                cleaned = cleaned.substring(start + 1, end).trim();
            }
        }

        // For now, create a single completion item
        // In the future, could parse multiple suggestions
        items.add(new GeminiCompletionItem(
                cleaned,
                context.getPrefix(),
                context.getCaretOffset(),
                100 // priority
        ));

        return items;
    }
}
