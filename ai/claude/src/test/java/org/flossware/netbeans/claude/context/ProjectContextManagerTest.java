/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.context;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ProjectContextManagerTest {
    @Test
    void testGetInstance() {
        ProjectContextManager manager = ProjectContextManager.getInstance();
        assertThat(manager).isNotNull();
    }

    @Test
    void testGetInstanceReturnsSame() {
        ProjectContextManager m1 = ProjectContextManager.getInstance();
        ProjectContextManager m2 = ProjectContextManager.getInstance();
        assertThat(m1).isSameAs(m2);
    }
}
