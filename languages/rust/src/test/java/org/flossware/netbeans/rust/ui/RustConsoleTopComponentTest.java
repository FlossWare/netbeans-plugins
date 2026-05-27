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

package org.flossware.netbeans.rust.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for RustConsoleTopComponent.
 */
class RustConsoleTopComponentTest {

    private RustConsoleTopComponent component;

    @BeforeEach
    void setUp() {
        component = new RustConsoleTopComponent();
    }

    @Test
    void testGetReplCommand() {
        assertThat(component.getReplCommand()).isEqualTo("evcxr");
    }

    @Test
    void testGetConsoleName() {
        assertThat(component.getConsoleName()).isEqualTo("Rust Console");
    }

    @Test
    void testGetIconPath() {
        assertThat(component.getIconPath()).isEqualTo("org/flossware/netbeans/rust/resources/rust-icon.png");
    }

    @Test
    void testGetStartupMessage() {
        String message = component.getStartupMessage();
        assertThat(message).isNotNull();
        assertThat(message).contains("evcxr");
    }

    @Test
    void testComponentName() {
        assertThat(component.getName()).isNotNull();
    }
}
