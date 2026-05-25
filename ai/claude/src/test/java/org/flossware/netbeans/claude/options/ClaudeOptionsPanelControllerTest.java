/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.options;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ClaudeOptionsPanelControllerTest {
    private ClaudeOptionsPanelController controller;

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        controller = new ClaudeOptionsPanelController();
    }

    @Test
    void testConstruction() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testGetComponent() {
        assertThat(controller.getComponent(null)).isNotNull();
    }

    @Test
    void testIsChanged() {
        assertThatCode(() -> controller.isChanged()).doesNotThrowAnyException();
    }

    @Test
    void testUpdate() {
        assertThatCode(() -> controller.update()).doesNotThrowAnyException();
    }

    @Test
    void testApplyChanges() {
        assertThatCode(() -> controller.applyChanges()).doesNotThrowAnyException();
    }
}
