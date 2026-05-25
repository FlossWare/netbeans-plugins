/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.erlang.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ErlangSettingsTest {
    @Test
    void testGetInstance_ReturnsSameInstance() {
        ErlangSettings instance1 = ErlangSettings.getInstance();
        ErlangSettings instance2 = ErlangSettings.getInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> ErlangSettings.getInstance()).doesNotThrowAnyException();
    }
}
