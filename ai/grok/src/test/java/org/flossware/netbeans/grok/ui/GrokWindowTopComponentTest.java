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

package org.flossware.netbeans.grok.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.Component;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GrokWindowTopComponent.
 * Note: UI testing in headless environment tests component construction and basic properties.
 */
class GrokWindowTopComponentTest {

    private GrokWindowTopComponent component;

    @BeforeEach
    void setUp() {
        // Create component in headless mode
        System.setProperty("java.awt.headless", "true");
        component = new GrokWindowTopComponent();
    }

    @Test
    void testComponentConstruction() {
        assertThat(component).isNotNull();
    }

    @Test
    void testComponentName() {
        assertThat(component.getName())
            .isNotEmpty();
    }

    @Test
    void testComponentTooltip() {
        assertThat(component.getToolTipText())
            .isNotEmpty();
    }

    @Test
    void testComponentHasChildren() {
        // Component should have UI elements
        assertThat(component.getComponentCount()).isGreaterThan(0);
    }

    @Test
    void testGetInstance_ReturnsSameInstance() {
        GrokWindowTopComponent instance1 = GrokWindowTopComponent.findInstance();
        GrokWindowTopComponent instance2 = GrokWindowTopComponent.findInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testComponentIsVisible_Initially() {
        // Component can be made visible
        assertThatCode(() -> component.setVisible(true))
            .doesNotThrowAnyException();
    }

    @Test
    void testPreferredId() {
        assertThat(component.preferredID())
            .isEqualTo("GrokWindowTopComponent");
    }

    @Test
    void testPersistenceType() {
        // TopComponent has persistence type set
        assertThat(component.getPersistenceType())
            .isEqualTo(org.openide.windows.TopComponent.PERSISTENCE_ALWAYS);
    }
}
