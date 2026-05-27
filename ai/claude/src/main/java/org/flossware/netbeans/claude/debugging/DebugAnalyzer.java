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

package org.flossware.netbeans.claude.debugging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.flossware.netbeans.claude.api.ClaudeService;
import org.flossware.netbeans.claude.util.CodeExtractor;
import org.flossware.netbeans.claude.util.CodeExtractor.CodeBlock;

/**
 * Analyzes errors and provides fix suggestions using Claude AI
 */
public class DebugAnalyzer {

    private static final Pattern CONFIDENCE_PATTERN = Pattern.compile(
        "confidence[:\\s]+(\\d+)%?",
        Pattern.CASE_INSENSITIVE
    );

    private static DebugAnalyzer instance;

    private DebugAnalyzer() {
    }

    public static synchronized DebugAnalyzer getInstance() {
        if (instance == null) {
            instance = new DebugAnalyzer();
        }
        return instance;
    }

    /**
     * Analyze error asynchronously
     */
    public CompletableFuture<List<FixSuggestion>> analyzeErrorAsync(String errorText, String sourceCode) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return analyzeError(errorText, sourceCode);
            } catch (Exception e) {
                throw new RuntimeException("Failed to analyze error", e);
            }
        });
    }

    /**
     * Analyze error and provide fix suggestions
     */
    public List<FixSuggestion> analyzeError(String errorText, String sourceCode) throws Exception {
        StackTraceInfo trace = StackTraceParser.parse(errorText);

        String prompt = buildAnalysisPrompt(trace, errorText, sourceCode);

        String response = ClaudeService.getInstance()
                .sendMessageAsync(prompt)
                .get();

        return parseSuggestions(response);
    }

    /**
     * Build prompt for Claude to analyze error
     */
    String buildAnalysisPrompt(StackTraceInfo trace, String errorText, String sourceCode) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Analyze this error and suggest fixes:\n\n");

        prompt.append("ERROR TYPE: ").append(trace.getExceptionType()).append("\n");
        prompt.append("MESSAGE: ").append(trace.getMessage()).append("\n\n");

        if (trace.hasLocation()) {
            prompt.append("LOCATION: ").append(trace.getFile())
                    .append(":").append(trace.getLine()).append("\n\n");
        }

        prompt.append("STACK TRACE:\n");
        prompt.append(errorText).append("\n\n");

        if (sourceCode != null && !sourceCode.trim().isEmpty()) {
            prompt.append("RELEVANT CODE:\n```java\n");
            prompt.append(sourceCode);
            prompt.append("\n```\n\n");
        }

        prompt.append("Provide:\n");
        prompt.append("1. Root cause explanation\n");
        prompt.append("2. Step-by-step fix suggestions (number each suggestion)\n");
        prompt.append("3. Code changes needed (use ```java code blocks)\n");
        prompt.append("4. Include confidence level (0-100%) for each suggestion\n");
        prompt.append("5. Prevention tips\n");

        return prompt.toString();
    }

    /**
     * Parse fix suggestions from Claude's response
     */
    List<FixSuggestion> parseSuggestions(String response) {
        List<FixSuggestion> suggestions = new ArrayList<>();

        List<CodeBlock> codeBlocks = CodeExtractor.extractCodeBlocks(response);

        String[] sections = response.split("\\d+\\.");
        int blockIndex = 0;

        for (int i = 1; i < sections.length && i <= 5; i++) {
            String section = sections[i].trim();
            if (section.isEmpty()) continue;

            String description = extractDescription(section);
            String fixCode = "";

            if (blockIndex < codeBlocks.size()) {
                CodeBlock block = codeBlocks.get(blockIndex);
                if ("java".equalsIgnoreCase(block.getLanguage())) {
                    fixCode = block.getCode();
                    blockIndex++;
                }
            }

            int confidence = extractConfidence(section);

            if (!description.isEmpty()) {
                suggestions.add(new FixSuggestion(description, fixCode, confidence));
            }
        }

        if (suggestions.isEmpty() && !codeBlocks.isEmpty()) {
            suggestions.add(new FixSuggestion(
                "Fix suggestion from analysis",
                codeBlocks.get(0).getCode(),
                80
            ));
        }

        return suggestions;
    }

    /**
     * Extract description from section
     */
    String extractDescription(String section) {
        String[] lines = section.split("\n");
        if (lines.length > 0) {
            String firstLine = lines[0].trim();
            if (firstLine.length() > 200) {
                return firstLine.substring(0, 200) + "...";
            }
            return firstLine;
        }
        return "";
    }

    /**
     * Extract confidence level from text
     */
    int extractConfidence(String text) {
        Matcher matcher = CONFIDENCE_PATTERN.matcher(text);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return 70;
            }
        }
        return 70;
    }
}
