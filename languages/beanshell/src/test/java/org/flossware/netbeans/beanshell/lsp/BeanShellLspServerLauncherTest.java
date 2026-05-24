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
