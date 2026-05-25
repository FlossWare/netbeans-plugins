/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.ruby.debugger;

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
class DebugRubyActionTest {

    @Mock
    private DataObject mockDataObject;

    @Mock
    private FileObject mockFileObject;

    private DebugRubyAction action;

    @BeforeEach
    void setUp() {
        action = new DebugRubyAction(mockDataObject);
    }

    @Test
    void testActionConstruction() {
        assertThat(action).isNotNull();
    }

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
}
