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

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JavadocGeneratorTest {

    @Test
    void testGetInstance_ReturnsSameInstance() {
        JavadocGenerator instance1 = JavadocGenerator.getInstance();
        JavadocGenerator instance2 = JavadocGenerator.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testBuildJavadocPrompt_SimpleMethod() {
        JavadocGenerator generator = JavadocGenerator.getInstance();

        MethodInfo method = new MethodInfo("testMethod", "void", Arrays.asList(), Arrays.asList());
        String code = "public void testMethod() { }";

        String prompt = generator.buildJavadocPrompt(method, code);

        assertThat(prompt).contains("Generate comprehensive Javadoc");
        assertThat(prompt).contains("public void testMethod()");
        assertThat(prompt).contains("standard javadoc");
    }

    @Test
    void testBuildJavadocPrompt_MethodWithParameters() {
        JavadocGenerator generator = JavadocGenerator.getInstance();

        MethodInfo method = new MethodInfo("add", "int",
                Arrays.asList("int a", "int b"), Arrays.asList());
        String code = "public int add(int a, int b) { return a + b; }";

        String prompt = generator.buildJavadocPrompt(method, code);

        assertThat(prompt).contains("@param for each parameter");
        assertThat(prompt).contains("@return with description");
    }

    @Test
    void testBuildJavadocPrompt_MethodWithThrows() {
        JavadocGenerator generator = JavadocGenerator.getInstance();

        MethodInfo method = new MethodInfo("read", "String",
                Arrays.asList(), Arrays.asList("IOException"));
        String code = "public String read() throws IOException { return null; }";

        String prompt = generator.buildJavadocPrompt(method, code);

        assertThat(prompt).contains("@throws for each exception");
    }

    @Test
    void testExtractJavadoc_StandardFormat() {
        JavadocGenerator generator = JavadocGenerator.getInstance();

        String response = "/**\n * This is a test method.\n * @param x the value\n */";

        String javadoc = generator.extractJavadoc(response);

        assertThat(javadoc).startsWith("/**");
        assertThat(javadoc).endsWith("*/");
        assertThat(javadoc).contains("@param x");
    }

    @Test
    void testExtractJavadoc_WithSurroundingText() {
        JavadocGenerator generator = JavadocGenerator.getInstance();

        String response = "Here's the javadoc:\n/**\n * Test method\n */\nEnd of javadoc.";

        String javadoc = generator.extractJavadoc(response);

        assertThat(javadoc).startsWith("/**");
        assertThat(javadoc).endsWith("*/");
    }

    @Test
    void testExtractJavadoc_NoJavadocPattern() {
        JavadocGenerator generator = JavadocGenerator.getInstance();

        String response = "This method adds two numbers together";

        String javadoc = generator.extractJavadoc(response);

        assertThat(javadoc).contains("/**");
        assertThat(javadoc).contains("*/");
        assertThat(javadoc).contains("adds two numbers");
    }
}
