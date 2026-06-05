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

package org.flossware.netbeans.gemini.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.swing.text.JTextComponent;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefactorWithGeminiActionTest {

    @Mock
    private JTextComponent mockComponent;

    private RefactorWithGeminiAction action;

    @BeforeEach
    void setUp() {
        when(mockComponent.getSelectionStart()).thenReturn(0);
        when(mockComponent.getSelectionEnd()).thenReturn(10);
        action = new RefactorWithGeminiAction(mockComponent);
    }

    @Test
    void testConstruction() {
        assertThat(action).isNotNull();
    }

    @Test
    void testConstruction_NullComponent() {
        RefactorWithGeminiAction nullAction = new RefactorWithGeminiAction(null);
        assertThat(nullAction.isEnabled()).isFalse();
    }

    @Test
    void testActionPerformed() {
        when(mockComponent.getSelectedText()).thenReturn("code");
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }
}
