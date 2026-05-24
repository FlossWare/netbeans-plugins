package org.flossware.netbeans.python.ui;

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Python Interactive Console (REPL).
 *
 * <p>Provides a Python interactive shell within NetBeans.</p>
 *
 * <p>This class extends {@link AbstractConsoleTopComponent} which provides
 * common REPL functionality including process management, I/O handling,
 * and UI components.</p>
 */
@TopComponent.Description(
    preferredID = "PythonConsoleTopComponent",
    iconBase = "org/flossware/netbeans/python/resources/python-icon.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.python.ui.PythonConsoleTopComponent")
@ActionReference(path = "Menu/Window", position = 333)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_PythonConsoleAction",
    preferredID = "PythonConsoleTopComponent"
)
@Messages({
    "CTL_PythonConsoleAction=Python Console",
    "CTL_PythonConsoleTopComponent=Python Console",
    "HINT_PythonConsoleTopComponent=Interactive Python console (REPL)"
})
public final class PythonConsoleTopComponent extends AbstractConsoleTopComponent {

    public PythonConsoleTopComponent() {
        super();
        setName(Bundle.CTL_PythonConsoleTopComponent());
        setToolTipText(Bundle.HINT_PythonConsoleTopComponent());
    }

    @Override
    protected String getReplCommand() {
        return "python";
    }

    @Override
    protected String[] getReplArgs() {
        return new String[]{"-i"};
    }

    @Override
    protected String getConsoleName() {
        return "Python Console";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/python/resources/python-icon.png";
    }

    @Override
    protected String getStartupMessage() {
        return "Type Python code and press Enter\n\n";
    }
}
