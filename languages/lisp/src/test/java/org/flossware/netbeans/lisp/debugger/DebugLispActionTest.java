/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.lisp.debugger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openide.loaders.DataObject;
import org.openide.filesystems.FileObject;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebugLispActionTest {

    @Mock
    private DataObject mockDataObject;

    @Mock
    private FileObject mockFileObject;

    private DebugLispAction action;

    @BeforeEach
    void setUp() {
        action = new DebugLispAction(mockDataObject);
    }

    @Test
    void testActionConstruction() {
        assertThat(action).isNotNull();
    }

    // Note: testActionPerformed tests are commented out because they require lisp debugger to be installed
    /*
    @Test
    void testActionPerformed_WithValidFile() {
        when(mockDataObject.getPrimaryFile()).thenReturn(mockFileObject);
        when(mockFileObject.getPath()).thenReturn("/test/script.txt");

        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }

    @Test
    void testActionPerformed_WithNullFile() {
        when(mockDataObject.getPrimaryFile()).thenReturn(mockFileObject);

        assertThatCode(() -> action.actionPerformed(null)).doesNotThrowAnyException();
    }
    */
}
