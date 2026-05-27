/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.mistral.options;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class MistralOptionsPanelControllerTest {

    private MistralOptionsPanelController controller;

    @BeforeEach
    void setUp() {
        controller = new MistralOptionsPanelController();
    }

    @Test
    void testConstruction() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testGetComponent_ReturnsNonNull() {
        assertThat(controller.getComponent(null)).isNotNull();
    }

    @Test
    void testUpdate_DoesNotThrow() {
        assertThatCode(() -> controller.update()).doesNotThrowAnyException();
    }

    @Test
    void testApplyChanges_DoesNotThrow() {
        assertThatCode(() -> controller.applyChanges()).doesNotThrowAnyException();
    }

    @Test
    void testCancel_DoesNotThrow() {
        assertThatCode(() -> controller.cancel()).doesNotThrowAnyException();
    }

    @Test
    void testIsValid_ReturnsBoolean() {
        boolean valid = controller.isValid();
        assertThat(valid).isNotNull();
    }

    @Test
    void testIsChanged_ReturnsBoolean() {
        boolean changed = controller.isChanged();
        assertThat(changed).isNotNull();
    }

    @Test
    void testAddPropertyChangeListener_DoesNotThrow() {
        assertThatCode(() -> controller.addPropertyChangeListener(null)).doesNotThrowAnyException();
    }

    @Test
    void testRemovePropertyChangeListener_DoesNotThrow() {
        assertThatCode(() -> controller.removePropertyChangeListener(null)).doesNotThrowAnyException();
    }
}
