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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CodeAnalyzerTest {

    @Test
    void testAnalyze_SimpleClass() {
        String code = "public class Calculator {\n" +
                     "    public int add(int a, int b) { return a + b; }\n" +
                     "    public int subtract(int a, int b) { return a - b; }\n" +
                     "}";

        CodeAnalysis result = CodeAnalyzer.analyze(code);

        assertThat(result).isNotNull();
        assertThat(result.getClassName()).isEqualTo("Calculator");
        assertThat(result.getMethods()).contains("add", "subtract");
    }

    @Test
    void testAnalyze_NullCode() {
        CodeAnalysis result = CodeAnalyzer.analyze(null);

        assertThat(result).isNotNull();
        assertThat(result.getClassName()).isEqualTo("Unknown");
        assertThat(result.getMethods()).isEmpty();
    }

    @Test
    void testAnalyze_EmptyCode() {
        CodeAnalysis result = CodeAnalyzer.analyze("");

        assertThat(result).isNotNull();
        assertThat(result.getClassName()).isEqualTo("Unknown");
    }

    @Test
    void testExtractClassName_Success() {
        String code = "public class TestClass { }";

        String className = CodeAnalyzer.extractClassName(code);

        assertThat(className).isEqualTo("TestClass");
    }

    @Test
    void testExtractClassName_WithModifiers() {
        String code = "public abstract class AbstractService { }";

        String className = CodeAnalyzer.extractClassName(code);

        assertThat(className).isEqualTo("AbstractService");
    }

    @Test
    void testExtractClassName_NoMatch() {
        String code = "interface NotAClass { }";

        String className = CodeAnalyzer.extractClassName(code);

        assertThat(className).isEqualTo("Unknown");
    }

    @Test
    void testExtractMethods_PublicMethods() {
        String code = "public class Test {\n" +
                     "    public void method1() {}\n" +
                     "    public String method2() { return null; }\n" +
                     "    public int method3(int x) { return x; }\n" +
                     "}";

        List<String> methods = CodeAnalyzer.extractMethods(code);

        assertThat(methods).containsExactlyInAnyOrder("method1", "method2", "method3");
    }

    @Test
    void testExtractMethods_PrivateMethods() {
        String code = "public class Test {\n" +
                     "    private void privateMethod() {}\n" +
                     "    protected void protectedMethod() {}\n" +
                     "}";

        List<String> methods = CodeAnalyzer.extractMethods(code);

        assertThat(methods).containsExactlyInAnyOrder("privateMethod", "protectedMethod");
    }

    @Test
    void testExtractMethods_StaticMethods() {
        String code = "public class Test {\n" +
                     "    public static void staticMethod() {}\n" +
                     "}";

        List<String> methods = CodeAnalyzer.extractMethods(code);

        assertThat(methods).contains("staticMethod");
    }

    @Test
    void testExtractMethods_NoMethods() {
        String code = "public class Test { }";

        List<String> methods = CodeAnalyzer.extractMethods(code);

        assertThat(methods).isEmpty();
    }

    @Test
    void testExtractFields_Success() {
        String code = "public class Test {\n" +
                     "    private String name;\n" +
                     "    private int count;\n" +
                     "    public static final String CONSTANT = \"value\";\n" +
                     "}";

        List<String> fields = CodeAnalyzer.extractFields(code);

        assertThat(fields).containsExactlyInAnyOrder("name", "count", "CONSTANT");
    }

    @Test
    void testExtractFields_NoFields() {
        String code = "public class Test { }";

        List<String> fields = CodeAnalyzer.extractFields(code);

        assertThat(fields).isEmpty();
    }

    @Test
    void testExtractDependencies_Success() {
        String code = "import java.util.List;\n" +
                     "import java.util.ArrayList;\n" +
                     "import org.junit.jupiter.api.Test;\n";

        List<String> deps = CodeAnalyzer.extractDependencies(code);

        assertThat(deps).containsExactlyInAnyOrder(
            "java.util.List",
            "java.util.ArrayList",
            "org.junit.jupiter.api.Test"
        );
    }

    @Test
    void testExtractDependencies_NoImports() {
        String code = "public class Test { }";

        List<String> deps = CodeAnalyzer.extractDependencies(code);

        assertThat(deps).isEmpty();
    }
}
