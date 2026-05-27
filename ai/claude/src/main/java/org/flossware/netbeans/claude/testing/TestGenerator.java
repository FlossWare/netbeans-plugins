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

package org.flossware.netbeans.claude.testing;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.flossware.netbeans.claude.api.ClaudeService;
import org.flossware.netbeans.claude.util.CodeExtractor;
import org.flossware.netbeans.claude.util.CodeExtractor.CodeBlock;

/**
 * Generates test code from source code using Claude AI
 */
public class TestGenerator {

    private static TestGenerator instance;

    private TestGenerator() {
    }

    public static synchronized TestGenerator getInstance() {
        if (instance == null) {
            instance = new TestGenerator();
        }
        return instance;
    }

    /**
     * Generate test code for the given source code
     */
    public CompletableFuture<String> generateTestAsync(String sourceCode, TestOptions options) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return generateTest(sourceCode, options);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate test", e);
            }
        });
    }

    /**
     * Generate test code synchronously
     */
    public String generateTest(String sourceCode, TestOptions options) throws Exception {
        CodeAnalysis analysis = CodeAnalyzer.analyze(sourceCode);

        String prompt = buildTestPrompt(analysis, options);

        String response = ClaudeService.getInstance()
                .sendMessageAsync(prompt)
                .get();

        return extractTestCode(response);
    }

    /**
     * Build prompt for Claude to generate tests
     */
    String buildTestPrompt(CodeAnalysis analysis, TestOptions options) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Generate comprehensive ").append(options.getFramework())
                .append(" tests for this Java code:\n\n");

        prompt.append("```java\n");
        prompt.append(analysis.getSourceCode());
        prompt.append("\n```\n\n");

        prompt.append("Requirements:\n");
        prompt.append("- Use ").append(options.getFramework())
                .append(" (@Test, @BeforeEach, @AfterEach)\n");
        prompt.append("- Use ").append(options.getMockingLibrary())
                .append(" for mocking dependencies\n");
        prompt.append("- Use ").append(options.getAssertionLibrary())
                .append(" for assertions\n");
        prompt.append("- Test all public methods\n");

        if (options.isIncludeEdgeCases()) {
            prompt.append("- Include edge cases and boundary conditions\n");
        }

        if (options.isIncludeErrorHandling()) {
            prompt.append("- Include error handling and exception tests\n");
        }

        prompt.append("- Coverage target: ").append(options.getCoverageTarget()).append("%\n");

        if (!analysis.getMethods().isEmpty()) {
            prompt.append("- Methods to test: ").append(analysis.getMethodNames()).append("\n");
        }

        prompt.append("\nGenerate complete test class with all necessary imports and annotations.");

        return prompt.toString();
    }

    /**
     * Extract test code from Claude's response
     */
    String extractTestCode(String response) {
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks(response);

        if (blocks.isEmpty()) {
            return "// No test code found in response\n// Response: " + response;
        }

        CodeBlock firstBlock = blocks.get(0);
        if ("java".equalsIgnoreCase(firstBlock.getLanguage())) {
            return firstBlock.getCode();
        }

        return firstBlock.getCode();
    }
}
