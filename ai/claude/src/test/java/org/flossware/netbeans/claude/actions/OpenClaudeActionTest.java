/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.actions;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class OpenClaudeActionTest {

    @Test
    void testConstruction() {
        OpenClaudeAction action = new OpenClaudeAction();
        assertThat(action).isNotNull();
    }

    @Test
    void testActionPerformed() {
        OpenClaudeAction action = new OpenClaudeAction();
        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_MultipleTimes() {
        OpenClaudeAction action = new OpenClaudeAction();
        assertThatCode(() -> {
            action.actionPerformed(null);
            action.actionPerformed(null);
            action.actionPerformed(null);
        }).doesNotThrowAnyException();
    }
}
