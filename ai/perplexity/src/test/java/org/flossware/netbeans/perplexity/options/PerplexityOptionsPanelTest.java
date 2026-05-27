/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.perplexity.options;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PerplexityOptionsPanelTest {

    @Mock
    private PerplexityOptionsPanelController mockController;

    private PerplexityOptionsPanel panel;

    @BeforeEach
    void setUp() {
        panel = new PerplexityOptionsPanel(mockController);
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
