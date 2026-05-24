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

package org.flossware.netbeans.mvel.lsp;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for MvelLspServerLauncher.
 */
class MvelLspServerLauncherTest {

    @Test
    void testGetServerCommands() {
        MvelLspServerLauncher launcher = new MvelLspServerLauncher();
        String[] commands = launcher.getServerCommands();

        assertThat(commands).isNotNull();
        // MVEL doesn't have a standard LSP server, so array should be empty
        assertThat(commands).isEmpty();
    }

    @Test
    void testGetLaunchArgs() {
        MvelLspServerLauncher launcher = new MvelLspServerLauncher();
        String[] args = launcher.getLaunchArgs("mvel-language-server");

        assertThat(args).isNotNull();
        assertThat(args).isEmpty();
    }

    @Test
    void testGetInstallationInstructions() {
        MvelLspServerLauncher launcher = new MvelLspServerLauncher();
        String instructions = launcher.getInstallationInstructions();

        assertThat(instructions).isNotNull();
        assertThat(instructions).contains("MVEL");
        assertThat(instructions).contains("Language Server");
        assertThat(instructions).contains("embedded");
    }
}
