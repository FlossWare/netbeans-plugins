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

package org.flossware.netbeans.claude.completion;

import java.net.URL;
import javax.swing.Action;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 * Documentation task for Claude completion items
 */
public class ClaudeCompletionDocumentation implements CompletionTask, CompletionDocumentation {

    private final String documentation;

    public ClaudeCompletionDocumentation(String documentation) {
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
        html.append("<b>Claude AI Suggestion:</b><br/><br/>");

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
