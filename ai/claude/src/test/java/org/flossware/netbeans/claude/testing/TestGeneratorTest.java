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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TestGeneratorTest {

    @Test
    void testGetInstance_ReturnsSameInstance() {
        TestGenerator instance1 = TestGenerator.getInstance();
        TestGenerator instance2 = TestGenerator.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testBuildTestPrompt_DefaultOptions() {
        TestGenerator generator = TestGenerator.getInstance();
        TestOptions options = new TestOptions();

        String sourceCode = "public class Calculator { public int add(int a, int b) { return a + b; } }";
        CodeAnalysis analysis = CodeAnalyzer.analyze(sourceCode);

        String prompt = generator.buildTestPrompt(analysis, options);

        assertThat(prompt).contains("JUnit 5");
        assertThat(prompt).contains("Mockito");
        assertThat(prompt).contains("AssertJ");
        assertThat(prompt).contains("95%");
        assertThat(prompt).contains("Calculator");
        assertThat(prompt).contains("add");
    }

    @Test
    void testBuildTestPrompt_CustomOptions() {
        TestGenerator generator = TestGenerator.getInstance();
        TestOptions options = new TestOptions();
        options.setFramework("JUnit 4");
        options.setMockingLibrary("EasyMock");
        options.setCoverageTarget(80);

        String sourceCode = "public class Test { }";
        CodeAnalysis analysis = CodeAnalyzer.analyze(sourceCode);

        String prompt = generator.buildTestPrompt(analysis, options);

        assertThat(prompt).contains("JUnit 4");
        assertThat(prompt).contains("EasyMock");
        assertThat(prompt).contains("80%");
    }

    @Test
    void testExtractTestCode_WithJavaCodeBlock() {
        TestGenerator generator = TestGenerator.getInstance();

        String response = "Here's the test:\n```java\n@Test\nvoid testAdd() {}\n```";

        String testCode = generator.extractTestCode(response);

        assertThat(testCode).contains("@Test");
        assertThat(testCode).contains("testAdd");
    }

    @Test
    void testExtractTestCode_NoCodeBlock() {
        TestGenerator generator = TestGenerator.getInstance();

        String response = "Sorry, I couldn't generate the test.";

        String testCode = generator.extractTestCode(response);

        assertThat(testCode).contains("No test code found");
    }
}
