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

package org.flossware.netbeans.chatgpt.completion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 * Individual completion item from ChatGPT
 */
public class ChatGPTCompletionItem implements CompletionItem {

    private final String text;
    private final String prefix;
    private final int caretOffset;
    private final int priority;
    private static final ImageIcon ICON = loadIcon();

    public ChatGPTCompletionItem(String text, String prefix, int caretOffset, int priority) {
        this.text = text;
        this.prefix = prefix;
        this.caretOffset = caretOffset;
        this.priority = priority;
    }

    private static ImageIcon loadIcon() {
        // Try to load ChatGPT icon, fallback to default
        ImageIcon icon = ImageUtilities.loadImageIcon(
                "org/flossware/netbeans/chatgpt/resources/chatgpt-icon.png", false);
        return icon;
    }

    @Override
    public void defaultAction(JTextComponent component) {
        try {
            Document doc = component.getDocument();

            // Remove the prefix
            int prefixStart = caretOffset - prefix.length();
            if (prefix.length() > 0) {
                doc.remove(prefixStart, prefix.length());
            }

            // Insert the completion
            doc.insertString(prefixStart, text, null);

        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void processKeyEvent(KeyEvent evt) {
        // No special key event processing
    }

    @Override
    public int getPreferredWidth(Graphics g, Font defaultFont) {
        return CompletionUtilities.getPreferredWidth(getLeftText(), getRightText(), g, defaultFont);
    }

    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor,
                      Color backgroundColor, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(
                ICON,
                getLeftText(),
                getRightText(),
                g, defaultFont,
                selected ? Color.WHITE : defaultColor,
                width, height, selected
        );
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return new ChatGPTCompletionDocumentation(text);
    }

    @Override
    public CompletionTask createToolTipTask() {
        return null;
    }

    @Override
    public boolean instantSubstitution(JTextComponent component) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return priority;
    }

    @Override
    public CharSequence getSortText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    }

    /**
     * Get left text (what's displayed in completion popup)
     */
    private String getLeftText() {
        // Show first line or truncated text
        String display = text;
        int newlineIndex = display.indexOf('\n');
        if (newlineIndex > 0) {
            display = display.substring(0, newlineIndex) + " ...";
        }
        if (display.length() > 50) {
            display = display.substring(0, 47) + "...";
        }
        return display;
    }

    /**
     * Get right text (annotation in completion popup)
     */
    private String getRightText() {
        return "ChatGPT AI";
    }
}
