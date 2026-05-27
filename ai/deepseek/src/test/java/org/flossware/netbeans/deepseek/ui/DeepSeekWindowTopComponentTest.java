/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.deepseek.ui;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class DeepSeekWindowTopComponentTest {

    @Test
    void testConstruction() {
        assertThatCode(() -> new DeepSeekWindowTopComponent()).doesNotThrowAnyException();
    }

    @Test
    void testGetName() {
        DeepSeekWindowTopComponent component = new DeepSeekWindowTopComponent();
        assertThat(component.getName()).isNotNull();
    }

    @Test
    void testGetToolTipText() {
        DeepSeekWindowTopComponent component = new DeepSeekWindowTopComponent();
        assertThat(component.getToolTipText()).isNotNull();
    }

    @Test
    void testFindInstance() {
        DeepSeekWindowTopComponent instance = DeepSeekWindowTopComponent.findInstance();
        assertThat(instance).isNotNull();
    }

    @Test
    void testFindInstance_ReturnsSame() {
        DeepSeekWindowTopComponent instance1 = DeepSeekWindowTopComponent.findInstance();
        DeepSeekWindowTopComponent instance2 = DeepSeekWindowTopComponent.findInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testPreferredID() {
        DeepSeekWindowTopComponent component = new DeepSeekWindowTopComponent();
        assertThat(component.preferredID()).isEqualTo("DeepSeekWindowTopComponent");
    }

    @Test
    void testPersistenceType_IsValid() {
        DeepSeekWindowTopComponent component = new DeepSeekWindowTopComponent();
        int persistenceType = component.getPersistenceType();
        assertThat(persistenceType).isIn(0, 1, 2);
    }

    @Test
    void testGetComponentCount() {
        DeepSeekWindowTopComponent component = new DeepSeekWindowTopComponent();
        assertThat(component.getComponentCount()).isGreaterThan(0);
    }

    @Test
    void testComponentOpened() {
        DeepSeekWindowTopComponent component = new DeepSeekWindowTopComponent();
        assertThatCode(() -> component.componentOpened()).doesNotThrowAnyException();
    }

    @Test
    void testComponentClosed() {
        DeepSeekWindowTopComponent component = new DeepSeekWindowTopComponent();
        assertThatCode(() -> component.componentClosed()).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_NullCode() {
        DeepSeekWindowTopComponent component = new DeepSeekWindowTopComponent();
        assertThatCode(() -> component.askAboutCode(null)).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_ValidCode() {
        DeepSeekWindowTopComponent component = new DeepSeekWindowTopComponent();
        assertThatCode(() -> component.askAboutCode("System.out.println();")).doesNotThrowAnyException();
    }
}
