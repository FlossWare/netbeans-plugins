package org.flossware.netbeans.beanshell.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for BeanShellSettings.
 */
class BeanShellSettingsTest {

    @Test
    void testGetInstance() {
        BeanShellSettings settings = BeanShellSettings.getInstance();
        assertThat(settings).isNotNull();
        assertThat(settings).isSameAs(BeanShellSettings.getInstance());
    }

    @Test
    void testDefaultBeanShellPath() {
        BeanShellSettings settings = BeanShellSettings.getInstance();
        String path = settings.getBeanShellPath();
        assertThat(path).isEqualTo("bsh");
    }
}
