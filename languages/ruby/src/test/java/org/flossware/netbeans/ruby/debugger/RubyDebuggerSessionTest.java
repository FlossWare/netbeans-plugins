/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.ruby.debugger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.*;

class RubyDebuggerSessionTest {
    @TempDir
    Path tempDir;
    private File testScript;
    private RubyDebuggerSession session;

    @BeforeEach
    void setUp() throws Exception {
        testScript = tempDir.resolve("test.txt").toFile();
        Files.writeString(testScript.toPath(), "test");
        session = new RubyDebuggerSession(testScript);
    }

    @Test
    void testSessionConstruction() {
        assertThat(session).isNotNull();
    }

    @Test
    void testStart_DoesNotThrow() {
        assertThatCode(() -> session.start()).doesNotThrowAnyException();
    }

    @Test
    void testStop_DoesNotThrow() {
        assertThatCode(() -> session.stop()).doesNotThrowAnyException();
    }
}
