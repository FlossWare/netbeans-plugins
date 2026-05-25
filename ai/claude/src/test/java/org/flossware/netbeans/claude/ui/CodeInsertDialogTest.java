/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.ui;

import org.flossware.netbeans.claude.util.CodeExtractor.CodeBlock;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class CodeInsertDialogTest {

    @Test
    void testConstruction() {
        CodeBlock codeBlock = new CodeBlock("java", "System.out.println();", 0, 10);
        assertThatCode(() -> new CodeInsertDialog(codeBlock)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_WithNullLanguage() {
        CodeBlock codeBlock = new CodeBlock(null, "code", 0, 10);
        assertThatCode(() -> new CodeInsertDialog(codeBlock)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_WithEmptyCode() {
        CodeBlock codeBlock = new CodeBlock("python", "", 0, 10);
        assertThatCode(() -> new CodeInsertDialog(codeBlock)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_WithLongCode() {
        String longCode = "public class Test {\n".repeat(1000);
        CodeBlock codeBlock = new CodeBlock("java", longCode, 0, 10);
        assertThatCode(() -> new CodeInsertDialog(codeBlock)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_WithSpecialCharacters() {
        CodeBlock codeBlock = new CodeBlock("java", "String s = \"<>&'\\\"test\";", 0, 10);
        assertThatCode(() -> new CodeInsertDialog(codeBlock)).doesNotThrowAnyException();
    }

    @Test
    void testShowAndInsert_NoActiveEditor() {
        CodeBlock codeBlock = new CodeBlock("java", "test", 0, 10);
        // This will show a warning dialog but won't crash
        assertThatCode(() -> CodeInsertDialog.showAndInsert(codeBlock)).doesNotThrowAnyException();
    }

    @Test
    void testGetComponentCount() {
        CodeBlock codeBlock = new CodeBlock("java", "code", 0, 10);
        CodeInsertDialog dialog = new CodeInsertDialog(codeBlock);
        assertThat(dialog.getComponentCount()).isGreaterThan(0);
    }

    @Test
    void testDialogLayout() {
        CodeBlock codeBlock = new CodeBlock("javascript", "console.log('test');", 0, 10);
        CodeInsertDialog dialog = new CodeInsertDialog(codeBlock);
        assertThat(dialog.getLayout()).isNotNull();
    }
}
