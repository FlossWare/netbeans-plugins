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

package org.flossware.netbeans.javascript.execution;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.openide.loaders.DataObject;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for RunJavaScriptAction.
 */
class RunJavaScriptActionTest {

    private RunJavaScriptAction action;
    private DataObject mockDataObject;

    @BeforeEach
    void setUp() {
        mockDataObject = Mockito.mock(DataObject.class);
        action = new RunJavaScriptAction(mockDataObject);
    }

    @Test
    void testGetInterpreterCommand() {
        assertThat(action.getInterpreterCommand()).isEqualTo("node");
    }

    @Test
    void testGetFileExtension() {
        assertThat(action.getFileExtension()).isEqualTo("js");
    }

    @Test
    void testGetLanguageName() {
        assertThat(action.getLanguageName()).isEqualTo("JavaScript");
    }
}
