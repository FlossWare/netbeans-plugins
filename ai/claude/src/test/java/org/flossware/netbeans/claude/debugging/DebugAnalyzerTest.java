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

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DebugAnalyzerTest {

    @Test
    void testGetInstance_ReturnsSameInstance() {
        DebugAnalyzer instance1 = DebugAnalyzer.getInstance();
        DebugAnalyzer instance2 = DebugAnalyzer.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testBuildAnalysisPrompt_Complete() {
        DebugAnalyzer analyzer = DebugAnalyzer.getInstance();

        StackTraceInfo trace = new StackTraceInfo(
            "NullPointerException",
            "Cannot invoke method",
            "Test.java",
            42,
            Arrays.asList("at Test.method")
        );

        String errorText = "java.lang.NullPointerException: Cannot invoke method";
        String sourceCode = "public void method() { obj.toString(); }";

        String prompt = analyzer.buildAnalysisPrompt(trace, errorText, sourceCode);

        assertThat(prompt).contains("ERROR TYPE: NullPointerException");
        assertThat(prompt).contains("MESSAGE: Cannot invoke method");
        assertThat(prompt).contains("LOCATION: Test.java:42");
        assertThat(prompt).contains("STACK TRACE:");
        assertThat(prompt).contains("RELEVANT CODE:");
        assertThat(prompt).contains("Root cause explanation");
    }

    @Test
    void testBuildAnalysisPrompt_NoLocation() {
        DebugAnalyzer analyzer = DebugAnalyzer.getInstance();

        StackTraceInfo trace = new StackTraceInfo(
            "RuntimeException",
            "Error occurred",
            null,
            0,
            Arrays.asList()
        );

        String prompt = analyzer.buildAnalysisPrompt(trace, "RuntimeException: Error", null);

        assertThat(prompt).contains("ERROR TYPE: RuntimeException");
        assertThat(prompt).doesNotContain("LOCATION:");
    }

    @Test
    void testParseSuggestions_WithCodeBlocks() {
        DebugAnalyzer analyzer = DebugAnalyzer.getInstance();

        String response = "1. Check for null\n```java\nif (obj != null) { obj.toString(); }\n```\n" +
                         "Confidence: 90%\n" +
                         "2. Use Optional\n```java\nOptional.ofNullable(obj).ifPresent(Object::toString);\n```\n" +
                         "Confidence: 85%";

        List<FixSuggestion> suggestions = analyzer.parseSuggestions(response);

        assertThat(suggestions).hasSize(2);
        assertThat(suggestions.get(0).getDescription()).contains("Check for null");
        assertThat(suggestions.get(0).hasCode()).isTrue();
        assertThat(suggestions.get(0).getConfidence()).isEqualTo(90);
    }

    @Test
    void testParseSuggestions_NoCodeBlocks() {
        DebugAnalyzer analyzer = DebugAnalyzer.getInstance();

        String response = "1. Add null check\n2. Use defensive programming";

        List<FixSuggestion> suggestions = analyzer.parseSuggestions(response);

        assertThat(suggestions).isNotEmpty();
    }

    @Test
    void testExtractDescription_Normal() {
        DebugAnalyzer analyzer = DebugAnalyzer.getInstance();

        String section = " Check for null values before dereferencing";

        String description = analyzer.extractDescription(section);

        assertThat(description).isEqualTo("Check for null values before dereferencing");
    }

    @Test
    void testExtractDescription_Long() {
        DebugAnalyzer analyzer = DebugAnalyzer.getInstance();

        String section = " " + "a".repeat(250);

        String description = analyzer.extractDescription(section);

        assertThat(description).hasSize(203); // 200 + "..."
        assertThat(description).endsWith("...");
    }

    @Test
    void testExtractConfidence_Found() {
        DebugAnalyzer analyzer = DebugAnalyzer.getInstance();

        int confidence = analyzer.extractConfidence("This fix has confidence: 85%");

        assertThat(confidence).isEqualTo(85);
    }

    @Test
    void testExtractConfidence_NotFound() {
        DebugAnalyzer analyzer = DebugAnalyzer.getInstance();

        int confidence = analyzer.extractConfidence("No confidence mentioned");

        assertThat(confidence).isEqualTo(70);
    }
}
