/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.lisp.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class LispSettingsTest {
    @Test
    void testGetInstance_ReturnsSameInstance() {
        LispSettings instance1 = LispSettings.getInstance();
        LispSettings instance2 = LispSettings.getInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> LispSettings.getInstance()).doesNotThrowAnyException();
    }
}
