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

package org.flossware.netbeans.beanshell.lsp;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for BeanShellLspServerLauncher.
 */
class BeanShellLspServerLauncherTest {

    @Test
    void testGetServerCommands() {
        BeanShellLspServerLauncher launcher = new BeanShellLspServerLauncher();
        String[] commands = launcher.getServerCommands();

        // BeanShell does not have a standard LSP server, so should return null
        assertThat(commands).isNull();
    }

    @Test
    void testGetLaunchArgs() {
        BeanShellLspServerLauncher launcher = new BeanShellLspServerLauncher();
        String[] args = launcher.getLaunchArgs("bsh");

        assertThat(args).isNotNull();
        assertThat(args).isEmpty();
    }

    @Test
    void testGetInstallationInstructions() {
        BeanShellLspServerLauncher launcher = new BeanShellLspServerLauncher();
        String instructions = launcher.getInstallationInstructions();

        assertThat(instructions).isNotNull();
        assertThat(instructions).contains("BeanShell");
        assertThat(instructions).contains("Language Server Not Available");
    }
}
