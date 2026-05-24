package org.flossware.netbeans.chatgpt.util;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.cookies.EditorCookie;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;

/**
 * Utility for interacting with NetBeans editor
 */
public class EditorUtil {

    /**
     * Get the currently active editor component
     */
    public static JTextComponent getActiveEditor() {
        return EditorRegistry.lastFocusedComponent();
    }

    /**
     * Get the currently selected text in the active editor
     */
    public static String getSelectedText() {
        JTextComponent editor = getActiveEditor();
        if (editor != null) {
            return editor.getSelectedText();
        }
        return null;
    }

    /**
     * Insert text at the current cursor position
     */
    public static void insertTextAtCursor(String text) {
        JTextComponent editor = getActiveEditor();
        if (editor != null) {
            try {
                Document doc = editor.getDocument();
                int caretPos = editor.getCaretPosition();
                doc.insertString(caretPos, text, null);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /**
     * Replace the currently selected text
     */
    public static void replaceSelectedText(String replacement) {
        JTextComponent editor = getActiveEditor();
        if (editor != null && editor.getSelectionStart() != editor.getSelectionEnd()) {
            try {
                Document doc = editor.getDocument();
                int start = editor.getSelectionStart();
                int end = editor.getSelectionEnd();
                doc.remove(start, end - start);
                doc.insertString(start, replacement, null);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /**
     * Insert text at a specific line
     */
    public static void insertTextAtLine(int lineNumber, String text) {
        JTextComponent editor = getActiveEditor();
        if (editor != null) {
            try {
                Document doc = editor.getDocument();
                if (doc instanceof StyledDocument) {
                    StyledDocument styledDoc = (StyledDocument) doc;
                    int offset = NbDocument.findLineOffset(styledDoc, lineNumber);
                    doc.insertString(offset, text, null);
                }
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /**
     * Get the document of the active editor
     */
    public static Document getActiveDocument() {
        JTextComponent editor = getActiveEditor();
        return editor != null ? editor.getDocument() : null;
    }

    /**
     * Check if there's an active editor
     */
    public static boolean hasActiveEditor() {
        return getActiveEditor() != null;
    }

    /**
     * Get all text from the active editor
     */
    public static String getAllText() {
        JTextComponent editor = getActiveEditor();
        if (editor != null) {
            return editor.getText();
        }
        return null;
    }
}
