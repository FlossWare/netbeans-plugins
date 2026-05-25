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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GrokOptionsPanel.
 * Note: UI testing in headless environment tests panel construction and basic properties.
 */
@ExtendWith(MockitoExtension.class)
class GrokOptionsPanelTest {

    @Mock
    private GrokOptionsPanelController mockController;

    private GrokOptionsPanel panel;

    @BeforeEach
    void setUp() {
        // Create panel in headless mode
        System.setProperty("java.awt.headless", "true");
        panel = new GrokOptionsPanel(mockController);
    }

    @Test
    void testPanelConstruction() {
        assertThat(panel).isNotNull();
    }

    @Test
    void testPanelHasComponents() {
        // Panel should have UI components
        assertThat(panel.getComponentCount()).isGreaterThan(0);
    }

    @Test
    void testLoad_DoesNotThrow() {
        assertThatCode(() -> panel.load())
            .doesNotThrowAnyException();
    }

    @Test
    void testStore_DoesNotThrow() {
        assertThatCode(() -> panel.store())
            .doesNotThrowAnyException();
    }

    @Test
    void testValid_ReturnsTrue() {
        // Panel should be valid by default
        assertThat(panel.valid()).isTrue();
    }
}
