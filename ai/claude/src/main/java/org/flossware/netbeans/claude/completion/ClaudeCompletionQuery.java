package org.flossware.netbeans.claude.completion;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.claude.api.ClaudeService;
import org.flossware.netbeans.claude.completion.CompletionContextBuilder.CompletionContext;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.openide.util.Exceptions;

/**
 * Async query for Claude code completions
 */
public class ClaudeCompletionQuery extends AsyncCompletionQuery {

    private final JTextComponent component;

    public ClaudeCompletionQuery(JTextComponent component) {
        this.component = component;
    }

    @Override
    protected void query(CompletionResultSet resultSet, javax.swing.text.Document doc, int caretOffset) {
        try {
            // Build context from editor
            CompletionContextBuilder builder = new CompletionContextBuilder(component, caretOffset);
            CompletionContext context = builder.build();

            // Check minimum characters
            if (context.getPrefix().length() < ClaudeCompletionSettings.getMinimumCharacters()) {
                resultSet.finish();
                return;
            }

            // Check cache first
            CompletionCache cache = CompletionCache.getInstance();
            String cacheKey = context.getTextBefore() + "|" + context.getPrefix();

            if (ClaudeCompletionSettings.isCacheEnabled()) {
                List<ClaudeCompletionItem> cached = cache.get(cacheKey);
                if (cached != null) {
                    cached.forEach(resultSet::addItem);
                    resultSet.finish();
                    return;
                }
            }

            // Query Claude
            String prompt = context.buildPrompt();
            ClaudeService service = ClaudeService.getInstance();

            if (!service.isConfigured()) {
                resultSet.finish();
                return;
            }

            // Get completions from Claude
            service.sendMessageAsync(prompt).thenAccept(response -> {
                List<ClaudeCompletionItem> items = parseCompletions(response, context);

                // Add to cache
                if (ClaudeCompletionSettings.isCacheEnabled()) {
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
     * Parse Claude's response into completion items
     */
    private List<ClaudeCompletionItem> parseCompletions(String response, CompletionContext context) {
        List<ClaudeCompletionItem> items = new ArrayList<>();

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
        items.add(new ClaudeCompletionItem(
                cleaned,
                context.getPrefix(),
                context.getCaretOffset(),
                100 // priority
        ));

        return items;
    }
}
