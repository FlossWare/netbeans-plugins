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

package org.flossware.netbeans.gemini.context;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for ProjectContextManager.
 * Tests singleton behavior and basic API.
 */
class ProjectContextManagerTest {

    @Test
    void testGetInstance_ReturnsNonNull() {
        ProjectContextManager manager = ProjectContextManager.getInstance();
        assertThat(manager).isNotNull();
    }

    @Test
    void testGetInstance_ReturnsSameInstance() {
        ProjectContextManager manager1 = ProjectContextManager.getInstance();
        ProjectContextManager manager2 = ProjectContextManager.getInstance();
        assertThat(manager1).isSameAs(manager2);
    }

    @Test
    void testBuildContextForQuery_ReturnsString() {
        ProjectContextManager manager = ProjectContextManager.getInstance();
        String result = manager.buildContextForQuery("test query");
        assertThat(result).isNotNull();
    }

    @Test
    void testGetProjectSummary_ReturnsString() {
        ProjectContextManager manager = ProjectContextManager.getInstance();
        String summary = manager.getProjectSummary();
        assertThat(summary).isNotNull();
    }

    @Test
    void testGetActiveProject_WithoutPlatform() {
        ProjectContextManager manager = ProjectContextManager.getInstance();
        // Without NetBeans platform, should return null
        assertThatCode(() -> manager.getActiveProject()).doesNotThrowAnyException();
    }

    @Test
    void testGetActiveFile_WithoutPlatform() {
        ProjectContextManager manager = ProjectContextManager.getInstance();
        assertThatCode(() -> manager.getActiveFile()).doesNotThrowAnyException();
    }

    @Test
    void testGetActiveProjectContext_WithoutPlatform() {
        ProjectContextManager manager = ProjectContextManager.getInstance();
        assertThatCode(() -> manager.getActiveProjectContext()).doesNotThrowAnyException();
    }
}
