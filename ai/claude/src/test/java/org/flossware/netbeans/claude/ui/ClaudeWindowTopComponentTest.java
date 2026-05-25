/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.ui;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ClaudeWindowTopComponentTest {

    @Test
    void testConstruction() {
        assertThatCode(() -> new ClaudeWindowTopComponent()).doesNotThrowAnyException();
    }

    @Test
    void testGetName() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThat(component.getName()).isNotNull();
    }

    @Test
    void testGetToolTipText() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThat(component.getToolTipText()).isNotNull();
    }

    @Test
    void testFindInstance() {
        ClaudeWindowTopComponent instance = ClaudeWindowTopComponent.findInstance();
        assertThat(instance).isNotNull();
    }

    @Test
    void testFindInstance_ReturnsSame() {
        ClaudeWindowTopComponent instance1 = ClaudeWindowTopComponent.findInstance();
        ClaudeWindowTopComponent instance2 = ClaudeWindowTopComponent.findInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testPreferredID() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThat(component.preferredID()).isEqualTo("ClaudeWindowTopComponent");
    }

    @Test
    void testPersistenceType_IsValid() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        int persistenceType = component.getPersistenceType();
        assertThat(persistenceType).isIn(0, 1, 2); // NEVER, ALWAYS, or ONLY_OPENED
    }

    @Test
    void testGetComponentCount() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThat(component.getComponentCount()).isGreaterThan(0);
    }

    @Test
    void testComponentOpened() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.componentOpened()).doesNotThrowAnyException();
    }

    @Test
    void testComponentClosed() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.componentClosed()).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_NullCode() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.askAboutCode(null)).doesNotThrowAnyException();
    }

    @Test
    void testAskAboutCode_ValidCode() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.askAboutCode("System.out.println();")).doesNotThrowAnyException();
    }

    @Test
    void testExplainCode_NullCode() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.explainCode(null)).doesNotThrowAnyException();
    }

    @Test
    void testExplainCode_ValidCode() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.explainCode("for(int i=0;i<10;i++){}")).doesNotThrowAnyException();
    }

    @Test
    void testRefactorCode_NullCode() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.refactorCode(null)).doesNotThrowAnyException();
    }

    @Test
    void testRefactorCode_ValidCode() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.refactorCode("public void test(){}")).doesNotThrowAnyException();
    }

    @Test
    void testShowProjectContext_NullContext() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.showProjectContext(null)).doesNotThrowAnyException();
    }

    @Test
    void testShowProjectContext_ValidContext() {
        ClaudeWindowTopComponent component = new ClaudeWindowTopComponent();
        assertThatCode(() -> component.showProjectContext("Project: Test")).doesNotThrowAnyException();
    }
}
