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

package org.flossware.netbeans.chatgpt.ui;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ChatGPTWindowTopComponentTest {

    @Test
    void testFindInstance() {
        // TopComponent instances are managed by NetBeans, test the lookup
        assertThatCode(() -> {
            var instance = ChatGPTWindowTopComponent.findInstance();
            assertThat(instance).isNotNull();
        }).doesNotThrowAnyException();
    }

    @Test
    void testGetDefault() {
        assertThatCode(() -> {
            var component = ChatGPTWindowTopComponent.getDefault();
            assertThat(component).isNotNull();
        }).doesNotThrowAnyException();
    }
}
