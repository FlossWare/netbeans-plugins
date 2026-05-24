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

package org.flossware.netbeans.groovy.completion;

import org.flossware.netbeans.common.completion.AbstractLspCompletionItem;

/**
 * Groovy LSP completion item implementation.
 *
 * <p>Represents a single code completion suggestion from the Groovy
 * language server.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GroovyLspCompletionItem extends AbstractLspCompletionItem {

    public GroovyLspCompletionItem(String text, int startOffset, int caretOffset) {
        super(text, startOffset, caretOffset);
    }

    @Override
    protected String getLanguageName() {
        return "Groovy";
    }
}
