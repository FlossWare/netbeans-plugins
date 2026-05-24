/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
