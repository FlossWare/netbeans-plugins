/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flossware.netbeans.groovy.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for GroovySettings.
 */
class GroovySettingsTest {

    @Test
    void testGetInstance() {
        GroovySettings settings = GroovySettings.getInstance();
        assertThat(settings).isNotNull();
        assertThat(settings).isSameAs(GroovySettings.getInstance());
    }

    @Test
    void testDefaultGroovyPath() {
        GroovySettings settings = GroovySettings.getInstance();
        String path = settings.getGroovyPath();
        assertThat(path).isEqualTo("groovy");
    }

    @Test
    void testDefaultLspServerPath() {
        GroovySettings settings = GroovySettings.getInstance();
        String path = settings.getLspServerPath();
        assertThat(path).isEqualTo("groovy-language-server");
    }
}
