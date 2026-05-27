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

package org.flossware.netbeans.claude.documentation;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.flossware.netbeans.claude.api.ClaudeService;

/**
 * Generates Javadoc documentation using Claude AI
 */
public class JavadocGenerator {

    private static final Pattern JAVADOC_PATTERN = Pattern.compile(
        "/\\*\\*[\\s\\S]*?\\*/",
        Pattern.MULTILINE
    );

    private static JavadocGenerator instance;

    private JavadocGenerator() {
    }

    public static synchronized JavadocGenerator getInstance() {
        if (instance == null) {
            instance = new JavadocGenerator();
        }
        return instance;
    }

    /**
     * Generate javadoc asynchronously
     */
    public CompletableFuture<String> generateJavadocAsync(String methodCode) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return generateJavadoc(methodCode);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate javadoc", e);
            }
        });
    }

    /**
     * Generate javadoc for a method
     */
    public String generateJavadoc(String methodCode) throws Exception {
        MethodInfo method = MethodSignatureExtractor.extract(methodCode);

        if (method == null) {
            return "/** Generated javadoc - method signature not found */";
        }

        String prompt = buildJavadocPrompt(method, methodCode);

        String response = ClaudeService.getInstance()
                .sendMessageAsync(prompt)
                .get();

        return extractJavadoc(response);
    }

    /**
     * Build prompt for Claude to generate javadoc
     */
    String buildJavadocPrompt(MethodInfo method, String methodCode) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Generate comprehensive Javadoc for this method:\n\n");
        prompt.append("```java\n");
        prompt.append(methodCode.trim());
        prompt.append("\n```\n\n");

        prompt.append("Include:\n");
        prompt.append("- Clear description of what the method does\n");

        if (method.hasParameters()) {
            prompt.append("- @param for each parameter with description\n");
        }

        if (!"void".equals(method.getReturnType())) {
            prompt.append("- @return with description of what is returned\n");
        }

        if (method.hasExceptions()) {
            prompt.append("- @throws for each exception with description\n");
        }

        prompt.append("\nFormat as standard javadoc (/** ... */)");
        prompt.append("\nProvide ONLY the javadoc comment, no other text.");

        return prompt.toString();
    }

    /**
     * Extract javadoc from Claude's response
     */
    String extractJavadoc(String response) {
        Matcher matcher = JAVADOC_PATTERN.matcher(response);
        if (matcher.find()) {
            return matcher.group();
        }

        if (response.contains("/**") && response.contains("*/")) {
            int start = response.indexOf("/**");
            int end = response.indexOf("*/", start) + 2;
            return response.substring(start, end);
        }

        return "/**\n * " + response.trim() + "\n */";
    }
}
