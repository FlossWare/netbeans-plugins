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

package org.flossware.netbeans.claude.testing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TestOptionsTest {

    @Test
    void testDefaultOptions() {
        TestOptions options = new TestOptions();

        assertThat(options.getFramework()).isEqualTo("JUnit 5");
        assertThat(options.getMockingLibrary()).isEqualTo("Mockito");
        assertThat(options.getAssertionLibrary()).isEqualTo("AssertJ");
        assertThat(options.getCoverageTarget()).isEqualTo(95);
        assertThat(options.isIncludeEdgeCases()).isTrue();
        assertThat(options.isIncludeErrorHandling()).isTrue();
    }

    @Test
    void testSetFramework() {
        TestOptions options = new TestOptions();
        options.setFramework("JUnit 4");

        assertThat(options.getFramework()).isEqualTo("JUnit 4");
    }

    @Test
    void testSetMockingLibrary() {
        TestOptions options = new TestOptions();
        options.setMockingLibrary("EasyMock");

        assertThat(options.getMockingLibrary()).isEqualTo("EasyMock");
    }

    @Test
    void testSetAssertionLibrary() {
        TestOptions options = new TestOptions();
        options.setAssertionLibrary("Hamcrest");

        assertThat(options.getAssertionLibrary()).isEqualTo("Hamcrest");
    }

    @Test
    void testSetCoverageTarget() {
        TestOptions options = new TestOptions();
        options.setCoverageTarget(80);

        assertThat(options.getCoverageTarget()).isEqualTo(80);
    }

    @Test
    void testSetIncludeEdgeCases() {
        TestOptions options = new TestOptions();
        options.setIncludeEdgeCases(false);

        assertThat(options.isIncludeEdgeCases()).isFalse();
    }

    @Test
    void testSetIncludeErrorHandling() {
        TestOptions options = new TestOptions();
        options.setIncludeErrorHandling(false);

        assertThat(options.isIncludeErrorHandling()).isFalse();
    }
}
