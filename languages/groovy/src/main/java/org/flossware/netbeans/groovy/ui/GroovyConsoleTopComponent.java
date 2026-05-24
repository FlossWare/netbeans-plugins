package org.flossware.netbeans.groovy.ui;

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.flossware.netbeans.groovy.settings.GroovySettings;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Groovy interactive console window.
 *
 * <p>Provides an interactive REPL (Read-Eval-Print Loop) for executing
 * Groovy code snippets and seeing immediate results.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@TopComponent.Description(
    preferredID = "GroovyConsoleTopComponent",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
    mode = "output",
    openAtStartup = false
)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_GroovyConsoleAction",
    preferredID = "GroovyConsoleTopComponent"
)
@NbBundle.Messages({
    "CTL_GroovyConsoleAction=Groovy Console",
    "CTL_GroovyConsoleTopComponent=Groovy Console",
    "HINT_GroovyConsoleTopComponent=Interactive Groovy console"
})
public class GroovyConsoleTopComponent extends AbstractConsoleTopComponent {

    public GroovyConsoleTopComponent() {
        setName(Bundle.CTL_GroovyConsoleTopComponent());
        setToolTipText(Bundle.HINT_GroovyConsoleTopComponent());
    }

    @Override
    protected String[] getInterpreterCommand() {
        GroovySettings settings = GroovySettings.getInstance();
        String groovyPath = settings.getGroovyPath();

        if (groovyPath == null || groovyPath.isEmpty()) {
            groovyPath = "groovysh"; // Interactive shell
        } else if (!groovyPath.contains("groovysh")) {
            // If custom path doesn't include groovysh, try to find it
            groovyPath = "groovysh";
        }

        return new String[]{groovyPath};
    }

    @Override
    protected String getLanguageName() {
        return "Groovy";
    }

    @Override
    protected String getWelcomeMessage() {
        return "Groovy Interactive Console\nType Groovy expressions and press Enter to evaluate.\n";
    }
}
