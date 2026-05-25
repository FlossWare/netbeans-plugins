/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.kotlin.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class KotlinSettingsTest {
    @Test
    void testGetInstance_ReturnsSameInstance() {
        KotlinSettings instance1 = KotlinSettings.getInstance();
        KotlinSettings instance2 = KotlinSettings.getInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> KotlinSettings.getInstance()).doesNotThrowAnyException();
    }
}
