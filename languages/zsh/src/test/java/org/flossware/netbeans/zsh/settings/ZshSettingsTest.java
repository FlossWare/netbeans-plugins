/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.zsh.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ZshSettingsTest {
    @Test
    void testGetInstance_ReturnsSameInstance() {
        ZshSettings instance1 = ZshSettings.getInstance();
        ZshSettings instance2 = ZshSettings.getInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> ZshSettings.getInstance()).doesNotThrowAnyException();
    }
}
