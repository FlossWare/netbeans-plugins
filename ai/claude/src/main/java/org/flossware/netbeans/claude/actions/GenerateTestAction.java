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

package org.flossware.netbeans.claude.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.claude.testing.TestGenerator;
import org.flossware.netbeans.claude.testing.TestOptions;
import org.flossware.netbeans.claude.util.EditorUtil;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import javax.swing.text.Document;

/**
 * Action to generate test code from selected code
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.claude.actions.GenerateTestAction"
)
@ActionRegistration(
        displayName = "#CTL_GenerateTestAction",
        lazy = true
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1103)
})
@Messages("CTL_GenerateTestAction=Generate Test (Claude)")
public final class GenerateTestAction extends AbstractAction {

    private final JTextComponent component;

    public GenerateTestAction(JTextComponent component) {
        super(Bundle.CTL_GenerateTestAction());
        this.component = component;
        setEnabled(component != null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String sourceCode = component.getSelectedText();
        if (sourceCode == null || sourceCode.trim().isEmpty()) {
            sourceCode = EditorUtil.getAllText();
        }

        if (sourceCode == null || sourceCode.trim().isEmpty()) {
            NotifyDescriptor nd = new NotifyDescriptor.Message(
                    "No code selected or available",
                    NotifyDescriptor.WARNING_MESSAGE
            );
            DialogDisplayer.getDefault().notify(nd);
            return;
        }

        TestOptions options = new TestOptions();

        final String finalSourceCode = sourceCode;

        ProgressHandle handle = ProgressHandleFactory.createHandle("Generating test code...");
        handle.start();

        TestGenerator.getInstance().generateTestAsync(finalSourceCode, options)
                .thenAccept(testCode -> SwingUtilities.invokeLater(() -> {
                    handle.finish();

                    try {
                        FileObject testFile = createTestFile();
                        if (testFile != null) {
                            insertTestCode(testFile, testCode);
                            openTestFile(testFile);
                        }
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                        NotifyDescriptor nd = new NotifyDescriptor.Message(
                                "Failed to create test file: " + ex.getMessage(),
                                NotifyDescriptor.ERROR_MESSAGE
                        );
                        DialogDisplayer.getDefault().notify(nd);
                    }
                }))
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        handle.finish();
                        Exceptions.printStackTrace(ex);
                        NotifyDescriptor nd = new NotifyDescriptor.Message(
                                "Failed to generate test: " + ex.getMessage(),
                                NotifyDescriptor.ERROR_MESSAGE
                        );
                        DialogDisplayer.getDefault().notify(nd);
                    });
                    return null;
                });
    }

    private FileObject createTestFile() throws IOException {
        FileObject currentFile = getCurrentFileObject();
        if (currentFile == null) {
            return null;
        }

        String fileName = currentFile.getName();
        String testFileName = fileName + "Test";

        FileObject srcRoot = currentFile.getParent();
        while (srcRoot != null && !srcRoot.getName().equals("java")) {
            srcRoot = srcRoot.getParent();
        }

        if (srcRoot == null || srcRoot.getParent() == null) {
            return null;
        }

        FileObject mainDir = srcRoot.getParent();
        FileObject projectRoot = mainDir.getParent();

        FileObject testDir = projectRoot.getFileObject("src/test");
        if (testDir == null) {
            testDir = FileUtil.createFolder(projectRoot, "src/test");
        }

        FileObject testJavaDir = testDir.getFileObject("java");
        if (testJavaDir == null) {
            testJavaDir = FileUtil.createFolder(testDir, "java");
        }

        String packagePath = getPackagePath(currentFile, srcRoot);
        FileObject packageDir = testJavaDir;
        if (!packagePath.isEmpty()) {
            packageDir = FileUtil.createFolder(testJavaDir, packagePath);
        }

        FileObject testFile = packageDir.getFileObject(testFileName, "java");
        if (testFile == null) {
            testFile = packageDir.createData(testFileName, "java");
        }

        return testFile;
    }

    private String getPackagePath(FileObject file, FileObject root) {
        String fullPath = FileUtil.getRelativePath(root, file.getParent());
        return fullPath != null ? fullPath : "";
    }

    private FileObject getCurrentFileObject() {
        DataObject dob = (DataObject) component.getDocument().getProperty(Document.StreamDescriptionProperty);
        return dob != null ? dob.getPrimaryFile() : null;
    }

    private void insertTestCode(FileObject testFile, String testCode) throws IOException {
        try {
            DataObject dob = DataObject.find(testFile);
            EditorCookie ec = dob.getLookup().lookup(EditorCookie.class);
            if (ec != null) {
                javax.swing.text.Document doc = ec.openDocument();
                doc.remove(0, doc.getLength());
                doc.insertString(0, testCode, null);
                ec.saveDocument();
            }
        } catch (javax.swing.text.BadLocationException ex) {
            throw new IOException("Failed to insert test code", ex);
        }
    }

    private void openTestFile(FileObject testFile) {
        try {
            DataObject dob = DataObject.find(testFile);
            OpenCookie oc = dob.getLookup().lookup(OpenCookie.class);
            if (oc != null) {
                oc.open();
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
