/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.batch.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class BatchSettingsTest {
    @Test
    void testGetInstance_ReturnsSameInstance() {
        BatchSettings instance1 = BatchSettings.getInstance();
        BatchSettings instance2 = BatchSettings.getInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> BatchSettings.getInstance()).doesNotThrowAnyException();
    }
}
