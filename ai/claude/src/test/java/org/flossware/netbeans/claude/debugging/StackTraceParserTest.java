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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StackTraceParserTest {

    private static final String SAMPLE_STACK_TRACE =
            "java.lang.NullPointerException: Cannot invoke method on null object\n" +
            "    at com.example.Test.method(Test.java:42)\n" +
            "    at com.example.Main.main(Main.java:10)";

    @Test
    void testParse_NullPointerException() {
        StackTraceInfo info = StackTraceParser.parse(SAMPLE_STACK_TRACE);

        assertThat(info).isNotNull();
        assertThat(info.getExceptionType()).isEqualTo("NullPointerException");
        assertThat(info.getMessage()).contains("Cannot invoke method");
        assertThat(info.getFile()).isEqualTo("Test.java");
        assertThat(info.getLine()).isEqualTo(42);
        assertThat(info.getStackTrace()).hasSize(3);
    }

    @Test
    void testParse_NullInput() {
        StackTraceInfo info = StackTraceParser.parse(null);

        assertThat(info).isNotNull();
        assertThat(info.getExceptionType()).isEqualTo("Unknown");
        assertThat(info.getMessage()).isEqualTo("No error message");
    }

    @Test
    void testParse_EmptyInput() {
        StackTraceInfo info = StackTraceParser.parse("");

        assertThat(info).isNotNull();
        assertThat(info.getExceptionType()).isEqualTo("Unknown");
    }

    @Test
    void testExtractExceptionType_FullyQualified() {
        String trace = "java.lang.IllegalArgumentException: bad argument";

        String type = StackTraceParser.extractExceptionType(trace);

        assertThat(type).isEqualTo("IllegalArgumentException");
    }

    @Test
    void testExtractExceptionType_Simple() {
        String trace = "NullPointerException: null value";

        String type = StackTraceParser.extractExceptionType(trace);

        assertThat(type).isEqualTo("NullPointerException");
    }

    @Test
    void testExtractExceptionType_Error() {
        String trace = "java.lang.OutOfMemoryError: Java heap space";

        String type = StackTraceParser.extractExceptionType(trace);

        assertThat(type).isEqualTo("OutOfMemoryError");
    }

    @Test
    void testExtractExceptionType_NoMatch() {
        String trace = "This is not an exception";

        String type = StackTraceParser.extractExceptionType(trace);

        assertThat(type).isEqualTo("Unknown");
    }

    @Test
    void testExtractMessage_WithMessage() {
        String trace = "java.io.IOException: File not found";

        String message = StackTraceParser.extractMessage(trace);

        assertThat(message).isEqualTo("File not found");
    }

    @Test
    void testExtractMessage_EmptyMessage() {
        String trace = "java.lang.RuntimeException:";

        String message = StackTraceParser.extractMessage(trace);

        assertThat(message).isEqualTo("No message provided");
    }

    @Test
    void testExtractMessage_NoMessage() {
        String trace = "Not an exception message";

        String message = StackTraceParser.extractMessage(trace);

        assertThat(message).isEqualTo("No error message");
    }

    @Test
    void testExtractFile_Success() {
        String file = StackTraceParser.extractFile(SAMPLE_STACK_TRACE);

        assertThat(file).isEqualTo("Test.java");
    }

    @Test
    void testExtractFile_NoMatch() {
        String file = StackTraceParser.extractFile("No stack trace here");

        assertThat(file).isNull();
    }

    @Test
    void testExtractLine_Success() {
        int line = StackTraceParser.extractLine(SAMPLE_STACK_TRACE);

        assertThat(line).isEqualTo(42);
    }

    @Test
    void testExtractLine_NoMatch() {
        int line = StackTraceParser.extractLine("No stack trace here");

        assertThat(line).isEqualTo(0);
    }

    @Test
    void testExtractStackTrace_MultipleLines() {
        var lines = StackTraceParser.extractStackTrace(SAMPLE_STACK_TRACE);

        assertThat(lines).hasSize(3);
        assertThat(lines.get(0)).contains("NullPointerException");
        assertThat(lines.get(1)).contains("Test.method");
        assertThat(lines.get(2)).contains("Main.main");
    }

    @Test
    void testExtractStackTrace_EmptyString() {
        var lines = StackTraceParser.extractStackTrace("");

        assertThat(lines).isEmpty();
    }
}
