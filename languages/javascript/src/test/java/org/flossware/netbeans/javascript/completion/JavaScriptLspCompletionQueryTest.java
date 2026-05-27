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

package org.flossware.netbeans.javascript.completion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for JavaScriptLspCompletionQuery.
 */
class JavaScriptLspCompletionQueryTest {

    private JavaScriptLspCompletionQuery query;

    @BeforeEach
    void setUp() {
        query = new JavaScriptLspCompletionQuery();
    }

    @Test
    void testGetMimeType() {
        assertThat(query.getMimeType()).isEqualTo("text/javascript");
    }

    @Test
    void testQueryLspServer_NullFile_ReturnsEmptyList() {
        assertThat(query.queryLspServer(null, 0, 0)).isEmpty();
    }
}
