package org.flossware.netbeans.groovy.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for GroovySettings.
 */
class GroovySettingsTest {

    @Test
    void testGetInstance() {
        GroovySettings settings = GroovySettings.getInstance();
        assertThat(settings).isNotNull();
        assertThat(settings).isSameAs(GroovySettings.getInstance());
    }

    @Test
    void testDefaultGroovyPath() {
        GroovySettings settings = GroovySettings.getInstance();
        String path = settings.getGroovyPath();
        assertThat(path).isEqualTo("groovy");
    }

    @Test
    void testDefaultLspServerPath() {
        GroovySettings settings = GroovySettings.getInstance();
        String path = settings.getLspServerPath();
        assertThat(path).isEqualTo("groovy-language-server");
    }
}
