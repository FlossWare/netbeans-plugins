/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.options;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClaudeOptionsPanelTest {
    @Mock
    private ClaudeOptionsPanelController mockController;

    private ClaudeOptionsPanel panel;

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        panel = new ClaudeOptionsPanel(mockController);
    }

    @Test
    void testConstruction() {
        assertThat(panel).isNotNull();
    }

    @Test
    void testLoad() {
        assertThatCode(() -> panel.load()).doesNotThrowAnyException();
    }

    @Test
    void testStore() {
        assertThatCode(() -> panel.store()).doesNotThrowAnyException();
    }
}
