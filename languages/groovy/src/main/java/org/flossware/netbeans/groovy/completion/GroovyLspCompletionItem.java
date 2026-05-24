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
