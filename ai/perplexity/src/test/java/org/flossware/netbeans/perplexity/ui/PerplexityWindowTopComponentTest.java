/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.perplexity.ui;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PerplexityWindowTopComponentTest {

    @Test
    void testConstruction() {
        assertThatCode(() -> new PerplexityWindowTopComponent()).doesNotThrowAnyException();
    }

    @Test
    void testGetName() {
        PerplexityWindowTopComponent component = new PerplexityWindowTopComponent();
        assertThat(component.getName()).isNotNull();
    }

    @Test
    void testGetToolTipText() {
        PerplexityWindowTopComponent component = new PerplexityWindowTopComponent();
        assertThat(component.getToolTipText()).isNotNull();
    }

    @Test
    void testFindInstance() {
        PerplexityWindowTopComponent instance = PerplexityWindowTopComponent.findInstance();
        assertThat(instance).isNotNull();
    }

    @Test
    void testFindInstance_ReturnsSame() {
        PerplexityWindowTopComponent instance1 = PerplexityWindowTopComponent.findInstance();
        PerplexityWindowTopComponent instance2 = PerplexityWindowTopComponent.findInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testPreferredID() {
        PerplexityWindowTopComponent component = new PerplexityWindowTopComponent();
        assertThat(component.preferredID()).isEqualTo("PerplexityWindowTopComponent");
    }

    @Test
    void testPersistenceType_IsValid() {
        PerplexityWindowTopComponent component = new PerplexityWindowTopComponent();
        int persistenceType = component.getPersistenceType();
        assertThat(persistenceType).isIn(0, 1, 2);
    }

    @Test
    void testGetComponentCount() {
        PerplexityWindowTopComponent component = new PerplexityWindowTopComponent();
        assertThat(component.getComponentCount()).isGreaterThan(0);
    }

    @Test
    void testComponentOpened() {
        PerplexityWindowTopComponent component = new PerplexityWindowTopComponent();
        assertThatCode(() -> component.componentOpened()).doesNotThrowAnyException();
    }

    @Test
    void testComponentClosed() {
        PerplexityWindowTopComponent component = new PerplexityWindowTopComponent();
        assertThatCode(() -> component.componentClosed()).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_NullCode() {
        PerplexityWindowTopComponent component = new PerplexityWindowTopComponent();
        assertThatCode(() -> component.askAboutCode(null)).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_ValidCode() {
        PerplexityWindowTopComponent component = new PerplexityWindowTopComponent();
        assertThatCode(() -> component.askAboutCode("System.out.println();")).doesNotThrowAnyException();
    }
}
