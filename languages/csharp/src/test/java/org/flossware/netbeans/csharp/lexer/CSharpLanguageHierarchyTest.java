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

package org.flossware.netbeans.csharp.lexer;

import org.junit.jupiter.api.Test;
import org.netbeans.api.lexer.Language;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for CSharpLanguageHierarchy.
 */
class CSharpLanguageHierarchyTest {

    @Test
    void testLanguageCreation() {
        CSharpLanguageHierarchy hierarchy = new CSharpLanguageHierarchy();
        Language<CSharpTokenId> language = hierarchy.language();
        assertThat(language).isNotNull();
    }

    @Test
    void testMimeType() {
        CSharpLanguageHierarchy hierarchy = new CSharpLanguageHierarchy();
        assertThat(hierarchy.mimeType()).isEqualTo("text/x-csharp");
    }
}
