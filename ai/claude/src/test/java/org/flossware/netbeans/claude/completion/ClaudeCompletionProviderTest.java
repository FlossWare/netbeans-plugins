/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ClaudeCompletionProviderTest {

    private ClaudeCompletionProvider provider;

    @BeforeEach
    void setUp() {
        provider = new ClaudeCompletionProvider();
    }

    @Test
    void testConstruction() {
        assertThat(provider).isNotNull();
    }

    @Test
    void testGetAutoQueryTypes() {
        int types = provider.getAutoQueryTypes(null, null);
        assertThat(types).isGreaterThanOrEqualTo(0);
    }

    @Test
    void testCreateTask() {
        assertThatCode(() -> provider.createTask(0, null)).doesNotThrowAnyException();
    }

    @Test
    void testCreateTask_AllTypes() {
        for (int type = 0; type <= 15; type++) {
            int queryType = type;
            assertThatCode(() -> provider.createTask(queryType, null)).doesNotThrowAnyException();
        }
    }
}
