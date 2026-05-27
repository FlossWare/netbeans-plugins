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

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MethodSignatureExtractorTest {

    @Test
    void testExtract_SimpleMethod() {
        String code = "public void testMethod() { }";

        MethodInfo method = MethodSignatureExtractor.extract(code);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("testMethod");
        assertThat(method.getReturnType()).isEqualTo("void");
        assertThat(method.getParameters()).isEmpty();
        assertThat(method.getExceptions()).isEmpty();
    }

    @Test
    void testExtract_MethodWithParameters() {
        String code = "public int add(int a, int b) { return a + b; }";

        MethodInfo method = MethodSignatureExtractor.extract(code);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("add");
        assertThat(method.getReturnType()).isEqualTo("int");
        assertThat(method.getParameters()).hasSize(2);
        assertThat(method.getParameters()).contains("int a", "int b");
    }

    @Test
    void testExtract_MethodWithThrows() {
        String code = "public String read() throws IOException { return null; }";

        MethodInfo method = MethodSignatureExtractor.extract(code);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("read");
        assertThat(method.getExceptions()).hasSize(1);
        assertThat(method.getExceptions()).contains("IOException");
    }

    @Test
    void testExtract_StaticMethod() {
        String code = "public static void staticMethod() { }";

        MethodInfo method = MethodSignatureExtractor.extract(code);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("staticMethod");
    }

    @Test
    void testExtract_NullCode() {
        MethodInfo method = MethodSignatureExtractor.extract(null);

        assertThat(method).isNull();
    }

    @Test
    void testExtract_EmptyCode() {
        MethodInfo method = MethodSignatureExtractor.extract("");

        assertThat(method).isNull();
    }

    @Test
    void testExtract_NoMatch() {
        String code = "This is not a method";

        MethodInfo method = MethodSignatureExtractor.extract(code);

        assertThat(method).isNull();
    }

    @Test
    void testParseParameters_SingleParam() {
        List<String> params = MethodSignatureExtractor.parseParameters("int a");

        assertThat(params).hasSize(1);
        assertThat(params).contains("int a");
    }

    @Test
    void testParseParameters_MultipleParams() {
        List<String> params = MethodSignatureExtractor.parseParameters("String name, int age, boolean active");

        assertThat(params).hasSize(3);
        assertThat(params).containsExactly("String name", "int age", "boolean active");
    }

    @Test
    void testParseParameters_EmptyString() {
        List<String> params = MethodSignatureExtractor.parseParameters("");

        assertThat(params).isEmpty();
    }

    @Test
    void testParseParameters_Null() {
        List<String> params = MethodSignatureExtractor.parseParameters(null);

        assertThat(params).isEmpty();
    }

    @Test
    void testParseExceptions_Single() {
        List<String> exceptions = MethodSignatureExtractor.parseExceptions("IOException");

        assertThat(exceptions).hasSize(1);
        assertThat(exceptions).contains("IOException");
    }

    @Test
    void testParseExceptions_Multiple() {
        List<String> exceptions = MethodSignatureExtractor.parseExceptions("IOException, SQLException");

        assertThat(exceptions).hasSize(2);
        assertThat(exceptions).containsExactly("IOException", "SQLException");
    }

    @Test
    void testParseExceptions_Null() {
        List<String> exceptions = MethodSignatureExtractor.parseExceptions(null);

        assertThat(exceptions).isEmpty();
    }
}
