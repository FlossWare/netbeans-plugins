/*
 * Copyright 2026 FlossWare.
 */

package org.flossware.netbeans.common.debugger;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test coverage for AbstractDebuggerSession
 */
class AbstractDebuggerSessionTest {

    @TempDir
    File tempDir;

    private File testScript;

    @BeforeEach
    void setUp() throws IOException {
        testScript = new File(tempDir, "test.py");
        testScript.createNewFile();
    }

    @Test
    void testConstructor_NullScriptFile() {
        assertThatThrownBy(() -> new TestDebuggerSession(null, "Test"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("scriptFile cannot be null");
    }

    @Test
    void testConstructor_NullDebuggerName() {
        assertThatThrownBy(() -> new TestDebuggerSession(testScript, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("debuggerName cannot be null or empty");
    }

    @Test
    void testConstructor_EmptyDebuggerName() {
        assertThatThrownBy(() -> new TestDebuggerSession(testScript, ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("debuggerName cannot be null or empty");
    }

    @Test
    void testConstructor_ValidArguments() {
        TestDebuggerSession session = new TestDebuggerSession(testScript, "Test Debugger");

        assertThat(session.getScriptFile()).isEqualTo(testScript);
        assertThat(session.getDebuggerName()).isEqualTo("Test Debugger");
    }

    @Test
    void testIsActive_BeforeStart() {
        TestDebuggerSession session = new TestDebuggerSession(testScript, "Test");

        assertThat(session.isActive()).isFalse();
    }

    @Test
    void testStop_WithoutStart() {
        TestDebuggerSession session = new TestDebuggerSession(testScript, "Test");

        // Should not throw exception
        session.stop();
    }

    // Test debugger implementation for testing
    private static class TestDebuggerSession extends AbstractDebuggerSession {

        public TestDebuggerSession(File scriptFile, String debuggerName) {
            super(scriptFile, debuggerName);
        }

        @Override
        protected ProcessBuilder createProcessBuilder() throws IOException {
            // Simple echo command for testing
            ProcessBuilder pb = new ProcessBuilder("echo", "test");
            pb.redirectErrorStream(true);
            return pb;
        }
    }
}
