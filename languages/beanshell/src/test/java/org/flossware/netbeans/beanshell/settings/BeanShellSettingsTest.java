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

package org.flossware.netbeans.beanshell.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for BeanShellSettings.
 */
class BeanShellSettingsTest {

    @Test
    void testGetInstance() {
        BeanShellSettings settings = BeanShellSettings.getInstance();
        assertThat(settings).isNotNull();
        assertThat(settings).isSameAs(BeanShellSettings.getInstance());
    }

    @Test
    void testDefaultBeanShellPath() {
        BeanShellSettings settings = BeanShellSettings.getInstance();
        String path = settings.getBeanShellPath();
        assertThat(path).isEqualTo("bsh");
    }
}
