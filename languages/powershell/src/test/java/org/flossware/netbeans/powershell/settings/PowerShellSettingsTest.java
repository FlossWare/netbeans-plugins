/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.powershell.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PowerShellSettingsTest {
    @Test
    void testGetInstance_ReturnsSameInstance() {
        PowerShellSettings instance1 = PowerShellSettings.getInstance();
        PowerShellSettings instance2 = PowerShellSettings.getInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> PowerShellSettings.getInstance()).doesNotThrowAnyException();
    }
}
