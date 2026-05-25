/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.actions;

import java.awt.event.ActionEvent;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ShowProjectContextActionTest {

    @Test
    void testConstruction() {
        assertThatCode(() -> new ShowProjectContextAction()).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_DoesNotThrow() {
        ShowProjectContextAction action = new ShowProjectContextAction();
        ActionEvent mockEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "test");
        assertThatCode(() -> action.actionPerformed(mockEvent)).doesNotThrowAnyException();
    }
}
