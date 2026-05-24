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
