package org.flossware.netbeans.python.settings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for PythonSettings.
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class PythonSettingsTest {

    @Test
    void testGetPythonPath_DefaultValue() {
        String path = PythonSettings.getInstance().getPythonPath();
        assertThat(path).isEqualTo("python");
    }

    @Test
    void testGetLspServer_DefaultValue() {
        String server = PythonSettings.getInstance().getLspServer();
        assertThat(server).isEqualTo("auto");
    }

    @Test
    void testIsVenvAutoDetectEnabled_DefaultValue() {
        boolean enabled = PythonSettings.getInstance().isVenvAutoDetectEnabled();
        assertThat(enabled).isTrue();
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> PythonSettings.getInstance().setPythonPath("/usr/bin/python3")).doesNotThrowAnyException();
        assertThatCode(() -> PythonSettings.getInstance().setLspServer("pyright")).doesNotThrowAnyException();
        assertThatCode(() -> PythonSettings.getInstance().setVenvAutoDetectEnabled(false)).doesNotThrowAnyException();
    }
}
