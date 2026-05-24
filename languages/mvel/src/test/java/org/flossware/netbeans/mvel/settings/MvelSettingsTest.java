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

package org.flossware.netbeans.mvel.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for MvelSettings.
 */
class MvelSettingsTest {

    @Test
    void testGetInstance() {
        MvelSettings settings = MvelSettings.getInstance();
        assertThat(settings).isNotNull();
        assertThat(settings).isSameAs(MvelSettings.getInstance());
    }

    @Test
    void testDefaultMvelInterpreterPath() {
        MvelSettings settings = MvelSettings.getInstance();
        String path = settings.getMvelInterpreterPath();
        // MVEL doesn't have a standard interpreter, default is empty
        assertThat(path).isEmpty();
    }

    @Test
    void testDefaultLspServerPath() {
        MvelSettings settings = MvelSettings.getInstance();
        String path = settings.getLspServerPath();
        // MVEL doesn't have a standard LSP server, default is empty
        assertThat(path).isEmpty();
    }

    @Test
    void testSetMvelInterpreterPath() {
        MvelSettings settings = MvelSettings.getInstance();
        settings.setMvelInterpreterPath("/usr/local/bin/mvel-runner");
        assertThat(settings.getMvelInterpreterPath()).isEqualTo("/usr/local/bin/mvel-runner");
    }

    @Test
    void testSetLspServerPath() {
        MvelSettings settings = MvelSettings.getInstance();
        settings.setLspServerPath("/usr/local/bin/mvel-lsp");
        assertThat(settings.getLspServerPath()).isEqualTo("/usr/local/bin/mvel-lsp");
    }
}
