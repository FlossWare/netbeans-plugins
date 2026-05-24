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

package org.flossware.netbeans.groovy.lsp;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for GroovyLspServerLauncher.
 */
class GroovyLspServerLauncherTest {

    @Test
    void testGetServerCommands() {
        GroovyLspServerLauncher launcher = new GroovyLspServerLauncher();
        String[] commands = launcher.getServerCommands();

        assertThat(commands).isNotNull();
        assertThat(commands).contains("groovy-language-server");
    }

    @Test
    void testGetLaunchArgs() {
        GroovyLspServerLauncher launcher = new GroovyLspServerLauncher();
        String[] args = launcher.getLaunchArgs("groovy-language-server");

        assertThat(args).isNotNull();
        assertThat(args).isEmpty();
    }

    @Test
    void testGetInstallationInstructions() {
        GroovyLspServerLauncher launcher = new GroovyLspServerLauncher();
        String instructions = launcher.getInstallationInstructions();

        assertThat(instructions).isNotNull();
        assertThat(instructions).contains("groovy-language-server");
        assertThat(instructions).contains("npm install");
    }
}
