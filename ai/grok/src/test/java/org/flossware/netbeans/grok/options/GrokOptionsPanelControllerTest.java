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

package org.flossware.netbeans.grok.options;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JComponent;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GrokOptionsPanelController.
 * Note: UI controller testing in headless environment tests basic lifecycle methods.
 */
class GrokOptionsPanelControllerTest {

    private GrokOptionsPanelController controller;

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        controller = new GrokOptionsPanelController();
    }

    @Test
    void testControllerConstruction() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testGetComponent_ReturnsNonNull() {
        JComponent component = controller.getComponent(null);
        assertThat(component).isNotNull();
    }

    @Test
    void testGetComponent_ReturnsSameInstance() {
        JComponent component1 = controller.getComponent(null);
        JComponent component2 = controller.getComponent(null);
        assertThat(component1).isSameAs(component2);
    }

    @Test
    void testGetHelpCtx_ReturnsNonNull() {
        assertThat(controller.getHelpCtx()).isNotNull();
    }

    @Test
    void testIsValid_ReturnsTrue() {
        assertThat(controller.isValid()).isTrue();
    }

    @Test
    void testIsChanged_InitiallyFalse() {
        assertThat(controller.isChanged()).isFalse();
    }

    @Test
    void testUpdate_DoesNotThrow() {
        assertThatCode(() -> controller.update())
            .doesNotThrowAnyException();
    }

    @Test
    void testApplyChanges_DoesNotThrow() {
        assertThatCode(() -> controller.applyChanges())
            .doesNotThrowAnyException();
    }

    @Test
    void testCancel_DoesNotThrow() {
        assertThatCode(() -> controller.cancel())
            .doesNotThrowAnyException();
    }

    @Test
    void testAddPropertyChangeListener_DoesNotThrow() {
        assertThatCode(() -> controller.addPropertyChangeListener(evt -> {}))
            .doesNotThrowAnyException();
    }

    @Test
    void testRemovePropertyChangeListener_DoesNotThrow() {
        java.beans.PropertyChangeListener listener = evt -> {};
        controller.addPropertyChangeListener(listener);

        assertThatCode(() -> controller.removePropertyChangeListener(listener))
            .doesNotThrowAnyException();
    }
}
