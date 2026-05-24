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

import java.net.URL;
import javax.swing.Action;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 * Documentation task for ChatGPT completion items
 */
public class ChatGPTCompletionDocumentation implements CompletionTask, CompletionDocumentation {

    private final String documentation;

    public ChatGPTCompletionDocumentation(String documentation) {
        this.documentation = documentation;
    }

    @Override
    public void query(CompletionResultSet resultSet) {
        resultSet.setDocumentation(this);
        resultSet.finish();
    }

    @Override
    public void refresh(CompletionResultSet resultSet) {
        // No refresh needed
    }

    @Override
    public void cancel() {
        // Nothing to cancel
    }

    @Override
    public String getText() {
        return formatAsHtml(documentation);
    }

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public CompletionDocumentation resolveLink(String link) {
        return null;
    }

    @Override
    public Action getGotoSourceAction() {
        return null;
    }

    /**
     * Format documentation text as HTML
     */
    private String formatAsHtml(String text) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<div style='font-family: monospace; padding: 5px;'>");
        html.append("<b>ChatGPT AI Suggestion:</b><br/><br/>");

        // Convert code to HTML with basic formatting
        String escaped = text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br/>");

        html.append("<pre>").append(escaped).append("</pre>");
        html.append("</div>");
        html.append("</body></html>");

        return html.toString();
    }
}
