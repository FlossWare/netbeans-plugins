/*
 * Copyright 2026 FlossWare.
 */

package org.flossware.netbeans.lisp.debugger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle;

@ActionID(category = "Debug", id = "org.flossware.netbeans.lisp.debugger.DebugLispAction")
@ActionRegistration(displayName = "#CTL_DebugLispAction")
@ActionReference(path = "Loaders/text/x-lisp/Actions", position = 150)
@NbBundle.Messages("CTL_DebugLispAction=Debug Lisp Script")
public class DebugLispAction implements ActionListener {
    private final DataObject context;

    public DebugLispAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileObject fileObject = context.getPrimaryFile();
        File file = FileUtil.toFile(fileObject);
        if (file != null) {
            LispDebuggerSession session = new LispDebuggerSession(file);
            session.start();
        }
    }
}
