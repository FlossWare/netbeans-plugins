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

package org.flossware.netbeans.chatgpt.util;

import org.flossware.netbeans.chatgpt.util.CodeExtractor.CodeBlock;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class CodeExtractorTest {

    @Test
    void testExtractCodeBlocks_WithLanguage() {
        String text = "Here's some code:\n```java\npublic class Test {}\n```";
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks(text);

        assertThat(blocks).hasSize(1);
        CodeBlock block = blocks.get(0);
        assertThat(block.getLanguage()).isEqualTo("java");
        assertThat(block.getCode()).isEqualTo("public class Test {}\n");
    }

    @Test
    void testExtractCodeBlocks_WithoutLanguage() {
        String text = "```\nsome code\n```";
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks(text);

        assertThat(blocks).hasSize(1);
        assertThat(blocks.get(0).getLanguage()).isEqualTo("text");
        assertThat(blocks.get(0).getCode()).contains("some code");
    }

    @Test
    void testExtractCodeBlocks_Multiple() {
        String text = "```java\ncode1\n```\nSome text\n```python\ncode2\n```";
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks(text);

        assertThat(blocks).hasSize(2);
        assertThat(blocks.get(0).getLanguage()).isEqualTo("java");
        assertThat(blocks.get(1).getLanguage()).isEqualTo("python");
    }

    @Test
    void testExtractCodeBlocks_NoCodeBlocks() {
        String text = "Just plain text with no code blocks";
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks(text);

        assertThat(blocks).isEmpty();
    }

    @Test
    void testExtractCodeBlocks_EmptyString() {
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks("");
        assertThat(blocks).isEmpty();
    }

    @Test
    void testExtractCodeBlocks_EmptyCodeBlock() {
        String text = "```java\n```";
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks(text);

        assertThat(blocks).hasSize(1);
        assertThat(blocks.get(0).getCode()).isEmpty();
    }

    @Test
    void testHasCodeBlocks_True() {
        String text = "Some text ```code``` more text";
        assertThat(CodeExtractor.hasCodeBlocks(text)).isTrue();
    }

    @Test
    void testHasCodeBlocks_False() {
        String text = "Just plain text";
        assertThat(CodeExtractor.hasCodeBlocks(text)).isFalse();
    }

    @Test
    void testHasCodeBlocks_EmptyString() {
        assertThat(CodeExtractor.hasCodeBlocks("")).isFalse();
    }

    @Test
    void testGetFirstCodeBlock_Exists() {
        String text = "```java\nfirst\n```\n```python\nsecond\n```";
        CodeBlock first = CodeExtractor.getFirstCodeBlock(text);

        assertThat(first).isNotNull();
        assertThat(first.getLanguage()).isEqualTo("java");
        assertThat(first.getCode()).isEqualTo("first\n");
    }

    @Test
    void testGetFirstCodeBlock_NoBlocks() {
        CodeBlock first = CodeExtractor.getFirstCodeBlock("no code blocks");
        assertThat(first).isNull();
    }

    @Test
    void testStripCodeBlockMarkers_WithMarkers() {
        String input = "```java\ncode here\n```";
        String stripped = CodeExtractor.stripCodeBlockMarkers(input);
        assertThat(stripped).isEqualTo("code here\n");
    }

    @Test
    void testStripCodeBlockMarkers_NoMarkers() {
        String input = "plain code";
        String stripped = CodeExtractor.stripCodeBlockMarkers(input);
        assertThat(stripped).isEqualTo("plain code");
    }

    @Test
    void testStripCodeBlockMarkers_WithLanguageTag() {
        String input = "```python\nprint('hello')\n```";
        String stripped = CodeExtractor.stripCodeBlockMarkers(input);
        assertThat(stripped).isEqualTo("print('hello')\n");
    }

    @Test
    void testCodeBlock_GetLanguage() {
        CodeBlock block = new CodeBlock("java", "code", 0, 10);
        assertThat(block.getLanguage()).isEqualTo("java");
    }

    @Test
    void testCodeBlock_GetLanguage_NullDefault() {
        CodeBlock block = new CodeBlock(null, "code", 0, 10);
        assertThat(block.getLanguage()).isEqualTo("text");
    }

    @Test
    void testCodeBlock_GetCode() {
        CodeBlock block = new CodeBlock("java", "some code", 0, 10);
        assertThat(block.getCode()).isEqualTo("some code");
    }

    @Test
    void testCodeBlock_GetStartIndex() {
        CodeBlock block = new CodeBlock("java", "code", 5, 15);
        assertThat(block.getStartIndex()).isEqualTo(5);
    }

    @Test
    void testCodeBlock_GetEndIndex() {
        CodeBlock block = new CodeBlock("java", "code", 5, 15);
        assertThat(block.getEndIndex()).isEqualTo(15);
    }

    @Test
    void testCodeBlock_ToString() {
        CodeBlock block = new CodeBlock("python", "12345", 0, 10);
        String str = block.toString();
        assertThat(str).contains("python");
        assertThat(str).contains("5");
    }

    @Test
    void testExtractCodeBlocks_MultilineCode() {
        String text = "```java\nline1\nline2\nline3\n```";
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks(text);

        assertThat(blocks).hasSize(1);
        assertThat(blocks.get(0).getCode()).contains("line1");
        assertThat(blocks.get(0).getCode()).contains("line2");
        assertThat(blocks.get(0).getCode()).contains("line3");
    }

    @Test
    void testExtractCodeBlocks_MixedLanguages() {
        String text = "```java\nJava code\n```\nText\n```python\nPython code\n```\nMore text\n```javascript\nJS code\n```";
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks(text);

        assertThat(blocks).hasSize(3);
        assertThat(blocks.get(0).getLanguage()).isEqualTo("java");
        assertThat(blocks.get(1).getLanguage()).isEqualTo("python");
        assertThat(blocks.get(2).getLanguage()).isEqualTo("javascript");
    }

    @Test
    void testExtractCodeBlocks_IncompleteBlock() {
        String text = "```java\ncode without closing";
        assertThatCode(() -> CodeExtractor.extractCodeBlocks(text)).doesNotThrowAnyException();
    }

    @Test
    void testExtractCodeBlocks_Indices() {
        String text = "prefix```code```suffix";
        List<CodeBlock> blocks = CodeExtractor.extractCodeBlocks(text);

        assertThat(blocks).hasSize(1);
        CodeBlock block = blocks.get(0);
        assertThat(block.getStartIndex()).isEqualTo(6);
        assertThat(block.getEndIndex()).isGreaterThan(block.getStartIndex());
    }
}
