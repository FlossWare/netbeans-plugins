/*
 * Copyright 2026 FlossWare.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.flossware.netbeans.bash.debugger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for BashDebuggerSession.
 * Note: These tests verify session construction and basic lifecycle without spawning real processes.
 */
class BashDebuggerSessionTest {

    @TempDir
    Path tempDir;

    private File testScript;
    private BashDebuggerSession session;

    @BeforeEach
    void setUp() throws Exception {
        testScript = tempDir.resolve("test.sh").toFile();
        Files.writeString(testScript.toPath(), "#!/bin/bash\necho 'test'\n");
        session = new BashDebuggerSession(testScript);
    }

    @Test
    void testSessionConstruction() {
        assertThat(session).isNotNull();
    }

    @Test
    void testStart_DoesNotThrowWithValidScript() {
        // Note: Actual process execution may fail if bashdb not installed
        // Test verifies method can be called without errors in construction
        assertThatCode(() -> session.start()).doesNotThrowAnyException();
    }

    @Test
    void testStop_DoesNotThrow() {
        assertThatCode(() -> session.stop()).doesNotThrowAnyException();
    }

    @Test
    void testStopBeforeStart_DoesNotThrow() {
        BashDebuggerSession newSession = new BashDebuggerSession(testScript);
        assertThatCode(() -> newSession.stop()).doesNotThrowAnyException();
    }

    @Test
    void testSessionWithNonExistentScript() {
        File nonExistent = new File("/tmp/nonexistent-bash-script.sh");
        BashDebuggerSession invalidSession = new BashDebuggerSession(nonExistent);

        assertThatCode(() -> invalidSession.start()).doesNotThrowAnyException();
    }
}
