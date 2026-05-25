/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.batch.debugger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.*;

class BatchDebuggerSessionTest {
    @TempDir
    Path tempDir;
    private File testScript;
    private BatchDebuggerSession session;

    @BeforeEach
    void setUp() throws Exception {
        testScript = tempDir.resolve("test.txt").toFile();
        Files.writeString(testScript.toPath(), "test");
        session = new BatchDebuggerSession(testScript);
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
