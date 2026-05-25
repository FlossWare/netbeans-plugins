/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.lisp.debugger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.*;

class LispDebuggerSessionTest {
    @TempDir
    Path tempDir;
    private File testScript;
    private LispDebuggerSession session;

    @BeforeEach
    void setUp() throws Exception {
        testScript = tempDir.resolve("test.txt").toFile();
        Files.writeString(testScript.toPath(), "test");
        session = new LispDebuggerSession(testScript);
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
