/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.prolog.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PrologSettingsTest {
    @Test
    void testGetInstance_ReturnsSameInstance() {
        PrologSettings instance1 = PrologSettings.getInstance();
        PrologSettings instance2 = PrologSettings.getInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> PrologSettings.getInstance()).doesNotThrowAnyException();
    }
}
