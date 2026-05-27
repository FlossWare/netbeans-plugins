/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.mistral.ui;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class MistralWindowTopComponentTest {

    @Test
    void testConstruction() {
        assertThatCode(() -> new MistralWindowTopComponent()).doesNotThrowAnyException();
    }

    @Test
    void testGetName() {
        MistralWindowTopComponent component = new MistralWindowTopComponent();
        assertThat(component.getName()).isNotNull();
    }

    @Test
    void testGetToolTipText() {
        MistralWindowTopComponent component = new MistralWindowTopComponent();
        assertThat(component.getToolTipText()).isNotNull();
    }

    @Test
    void testFindInstance() {
        MistralWindowTopComponent instance = MistralWindowTopComponent.findInstance();
        assertThat(instance).isNotNull();
    }

    @Test
    void testFindInstance_ReturnsSame() {
        MistralWindowTopComponent instance1 = MistralWindowTopComponent.findInstance();
        MistralWindowTopComponent instance2 = MistralWindowTopComponent.findInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testPreferredID() {
        MistralWindowTopComponent component = new MistralWindowTopComponent();
        assertThat(component.preferredID()).isEqualTo("MistralWindowTopComponent");
    }

    @Test
    void testPersistenceType_IsValid() {
        MistralWindowTopComponent component = new MistralWindowTopComponent();
        int persistenceType = component.getPersistenceType();
        assertThat(persistenceType).isIn(0, 1, 2);
    }

    @Test
    void testGetComponentCount() {
        MistralWindowTopComponent component = new MistralWindowTopComponent();
        assertThat(component.getComponentCount()).isGreaterThan(0);
    }

    @Test
    void testComponentOpened() {
        MistralWindowTopComponent component = new MistralWindowTopComponent();
        assertThatCode(() -> component.componentOpened()).doesNotThrowAnyException();
    }

    @Test
    void testComponentClosed() {
        MistralWindowTopComponent component = new MistralWindowTopComponent();
        assertThatCode(() -> component.componentClosed()).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_NullCode() {
        MistralWindowTopComponent component = new MistralWindowTopComponent();
        assertThatCode(() -> component.askAboutCode(null)).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_ValidCode() {
        MistralWindowTopComponent component = new MistralWindowTopComponent();
        assertThatCode(() -> component.askAboutCode("System.out.println();")).doesNotThrowAnyException();
    }
}
