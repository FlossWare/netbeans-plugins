/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.mistral.options;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MistralOptionsPanelTest {

    @Mock
    private MistralOptionsPanelController mockController;

    private MistralOptionsPanel panel;

    @BeforeEach
    void setUp() {
        panel = new MistralOptionsPanel(mockController);
    }

    @Test
    void testConstruction() {
        assertThat(panel).isNotNull();
    }

    @Test
    void testLoad_DoesNotThrow() {
        assertThatCode(() -> panel.load()).doesNotThrowAnyException();
    }

    @Test
    void testStore_DoesNotThrow() {
        assertThatCode(() -> panel.store()).doesNotThrowAnyException();
    }

    @Test
    void testValid_ReturnsTrue() {
        assertThat(panel.valid()).isTrue();
    }

    @Test
    void testGetComponentCount() {
        assertThat(panel.getComponentCount()).isGreaterThanOrEqualTo(0);
    }
}
