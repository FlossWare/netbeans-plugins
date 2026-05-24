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
