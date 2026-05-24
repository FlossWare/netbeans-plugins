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
