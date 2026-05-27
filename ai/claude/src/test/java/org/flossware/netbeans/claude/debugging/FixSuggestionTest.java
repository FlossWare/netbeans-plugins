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

package org.flossware.netbeans.claude.debugging;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FixSuggestionTest {

    @Test
    void testConstructor_Normal() {
        FixSuggestion fix = new FixSuggestion("Add null check", "if (x != null)", 80);

        assertThat(fix.getDescription()).isEqualTo("Add null check");
        assertThat(fix.getFixCode()).isEqualTo("if (x != null)");
        assertThat(fix.getConfidence()).isEqualTo(80);
    }

    @Test
    void testConstructor_ConfidenceClamping() {
        FixSuggestion fix1 = new FixSuggestion("Fix 1", "code", -10);
        FixSuggestion fix2 = new FixSuggestion("Fix 2", "code", 150);

        assertThat(fix1.getConfidence()).isEqualTo(0);
        assertThat(fix2.getConfidence()).isEqualTo(100);
    }

    @Test
    void testHasCode_True() {
        FixSuggestion fix = new FixSuggestion("Fix", "some code", 50);

        assertThat(fix.hasCode()).isTrue();
    }

    @Test
    void testHasCode_False() {
        FixSuggestion fix1 = new FixSuggestion("Fix", null, 50);
        FixSuggestion fix2 = new FixSuggestion("Fix", "", 50);
        FixSuggestion fix3 = new FixSuggestion("Fix", "   ", 50);

        assertThat(fix1.hasCode()).isFalse();
        assertThat(fix2.hasCode()).isFalse();
        assertThat(fix3.hasCode()).isFalse();
    }

    @Test
    void testToString() {
        FixSuggestion fix = new FixSuggestion("Add validation", "validate()", 75);

        String str = fix.toString();

        assertThat(str).contains("Add validation");
        assertThat(str).contains("75%");
    }
}
