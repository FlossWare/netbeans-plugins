/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.cohere.ui;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class CohereWindowTopComponentTest {

    @Test
    void testConstruction() {
        assertThatCode(() -> new CohereWindowTopComponent()).doesNotThrowAnyException();
    }

    @Test
    void testGetName() {
        CohereWindowTopComponent component = new CohereWindowTopComponent();
        assertThat(component.getName()).isNotNull();
    }

    @Test
    void testGetToolTipText() {
        CohereWindowTopComponent component = new CohereWindowTopComponent();
        assertThat(component.getToolTipText()).isNotNull();
    }

    @Test
    void testFindInstance() {
        CohereWindowTopComponent instance = CohereWindowTopComponent.findInstance();
        assertThat(instance).isNotNull();
    }

    @Test
    void testFindInstance_ReturnsSame() {
        CohereWindowTopComponent instance1 = CohereWindowTopComponent.findInstance();
        CohereWindowTopComponent instance2 = CohereWindowTopComponent.findInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testPreferredID() {
        CohereWindowTopComponent component = new CohereWindowTopComponent();
        assertThat(component.preferredID()).isEqualTo("CohereWindowTopComponent");
    }

    @Test
    void testPersistenceType_IsValid() {
        CohereWindowTopComponent component = new CohereWindowTopComponent();
        int persistenceType = component.getPersistenceType();
        assertThat(persistenceType).isIn(0, 1, 2);
    }

    @Test
    void testGetComponentCount() {
        CohereWindowTopComponent component = new CohereWindowTopComponent();
        assertThat(component.getComponentCount()).isGreaterThan(0);
    }

    @Test
    void testComponentOpened() {
        CohereWindowTopComponent component = new CohereWindowTopComponent();
        assertThatCode(() -> component.componentOpened()).doesNotThrowAnyException();
    }

    @Test
    void testComponentClosed() {
        CohereWindowTopComponent component = new CohereWindowTopComponent();
        assertThatCode(() -> component.componentClosed()).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_NullCode() {
        CohereWindowTopComponent component = new CohereWindowTopComponent();
        assertThatCode(() -> component.askAboutCode(null)).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_ValidCode() {
        CohereWindowTopComponent component = new CohereWindowTopComponent();
        assertThatCode(() -> component.askAboutCode("System.out.println();")).doesNotThrowAnyException();
    }
}
