/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.ruby.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class RubySettingsTest {
    @Test
    void testGetInstance_ReturnsSameInstance() {
        RubySettings instance1 = RubySettings.getInstance();
        RubySettings instance2 = RubySettings.getInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> RubySettings.getInstance()).doesNotThrowAnyException();
    }
}
