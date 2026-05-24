package org.flossware.netbeans.beanshell.ui;

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.flossware.netbeans.beanshell.settings.BeanShellSettings;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * BeanShell interactive console window.
 *
 * <p>Provides an interactive REPL (Read-Eval-Print Loop) for executing
 * BeanShell code snippets and seeing immediate results.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@TopComponent.Description(
    preferredID = "BeanShellConsoleTopComponent",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
    mode = "output",
    openAtStartup = false
)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_BeanShellConsoleAction",
    preferredID = "BeanShellConsoleTopComponent"
)
@NbBundle.Messages({
    "CTL_BeanShellConsoleAction=BeanShell Console",
    "CTL_BeanShellConsoleTopComponent=BeanShell Console",
    "HINT_BeanShellConsoleTopComponent=Interactive BeanShell console"
})
public class BeanShellConsoleTopComponent extends AbstractConsoleTopComponent {

    public BeanShellConsoleTopComponent() {
        setName(Bundle.CTL_BeanShellConsoleTopComponent());
        setToolTipText(Bundle.HINT_BeanShellConsoleTopComponent());
    }

    @Override
    protected String[] getInterpreterCommand() {
        BeanShellSettings settings = BeanShellSettings.getInstance();
        String bshPath = settings.getBeanShellPath();

        if (bshPath == null || bshPath.isEmpty()) {
            bshPath = "bsh"; // Interactive shell
        }

        return new String[]{bshPath};
    }

    @Override
    protected String getLanguageName() {
        return "BeanShell";
    }

    @Override
    protected String getWelcomeMessage() {
        return "BeanShell Interactive Console\nType BeanShell expressions and press Enter to evaluate.\n";
    }
}
