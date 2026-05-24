/*
 * Copyright 2026 FlossWare.
 */

package org.flossware.netbeans.kotlin.debugger;

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

@ActionID(category = "Debug", id = "org.flossware.netbeans.kotlin.debugger.DebugKotlinAction")
@ActionRegistration(displayName = "#CTL_DebugKotlinAction")
@ActionReference(path = "Loaders/text/x-kotlin/Actions", position = 150)
@NbBundle.Messages("CTL_DebugKotlinAction=Run Kotlin Script")
public class DebugKotlinAction implements ActionListener {
    private final DataObject context;

    public DebugKotlinAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileObject fileObject = context.getPrimaryFile();
        File file = FileUtil.toFile(fileObject);
        if (file != null) {
            KotlinDebuggerSession session = new KotlinDebuggerSession(file);
            session.start();
        }
    }
}
