/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.openrouter.ui;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class OpenRouterWindowTopComponentTest {

    @Test
    void testConstruction() {
        assertThatCode(() -> new OpenRouterWindowTopComponent()).doesNotThrowAnyException();
    }

    @Test
    void testGetName() {
        OpenRouterWindowTopComponent component = new OpenRouterWindowTopComponent();
        assertThat(component.getName()).isNotNull();
    }

    @Test
    void testGetToolTipText() {
        OpenRouterWindowTopComponent component = new OpenRouterWindowTopComponent();
        assertThat(component.getToolTipText()).isNotNull();
    }

    @Test
    void testFindInstance() {
        OpenRouterWindowTopComponent instance = OpenRouterWindowTopComponent.findInstance();
        assertThat(instance).isNotNull();
    }

    @Test
    void testFindInstance_ReturnsSame() {
        OpenRouterWindowTopComponent instance1 = OpenRouterWindowTopComponent.findInstance();
        OpenRouterWindowTopComponent instance2 = OpenRouterWindowTopComponent.findInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testPreferredID() {
        OpenRouterWindowTopComponent component = new OpenRouterWindowTopComponent();
        assertThat(component.preferredID()).isEqualTo("OpenRouterWindowTopComponent");
    }

    @Test
    void testPersistenceType_IsValid() {
        OpenRouterWindowTopComponent component = new OpenRouterWindowTopComponent();
        int persistenceType = component.getPersistenceType();
        assertThat(persistenceType).isIn(0, 1, 2);
    }

    @Test
    void testGetComponentCount() {
        OpenRouterWindowTopComponent component = new OpenRouterWindowTopComponent();
        assertThat(component.getComponentCount()).isGreaterThan(0);
    }

    @Test
    void testComponentOpened() {
        OpenRouterWindowTopComponent component = new OpenRouterWindowTopComponent();
        assertThatCode(() -> component.componentOpened()).doesNotThrowAnyException();
    }

    @Test
    void testComponentClosed() {
        OpenRouterWindowTopComponent component = new OpenRouterWindowTopComponent();
        assertThatCode(() -> component.componentClosed()).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_NullCode() {
        OpenRouterWindowTopComponent component = new OpenRouterWindowTopComponent();
        assertThatCode(() -> component.askAboutCode(null)).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_ValidCode() {
        OpenRouterWindowTopComponent component = new OpenRouterWindowTopComponent();
        assertThatCode(() -> component.askAboutCode("System.out.println();")).doesNotThrowAnyException();
    }
}
