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

package org.flossware.netbeans.ai.core.logging;

import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for LoggerFactory.
 */
class LoggerFactoryTest {

    @BeforeEach
    void setUp() {
        LoggerFactory.clearCache();
    }

    @Test
    void testGetLoggerByClass() {
        Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

        assertThat(logger).isNotNull();
        assertThat(logger.getName()).isEqualTo(LoggerFactoryTest.class.getName());
    }

    @Test
    void testGetLoggerByName() {
        String loggerName = "org.flossware.test.logger";
        Logger logger = LoggerFactory.getLogger(loggerName);

        assertThat(logger).isNotNull();
        assertThat(logger.getName()).isEqualTo(loggerName);
    }

    @Test
    void testLoggerCaching() {
        Logger logger1 = LoggerFactory.getLogger(LoggerFactoryTest.class);
        Logger logger2 = LoggerFactory.getLogger(LoggerFactoryTest.class);

        assertThat(logger1).isSameAs(logger2);
    }

    @Test
    void testGetLoggerNullClassThrowsException() {
        assertThatThrownBy(() -> LoggerFactory.getLogger((Class<?>) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Class cannot be null");
    }

    @Test
    void testGetLoggerNullNameThrowsException() {
        assertThatThrownBy(() -> LoggerFactory.getLogger((String) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Logger name cannot be null");
    }

    @Test
    void testGetLoggerEmptyNameThrowsException() {
        assertThatThrownBy(() -> LoggerFactory.getLogger(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Logger name cannot be empty");
    }

    @Test
    void testGetLoggerBlankNameThrowsException() {
        assertThatThrownBy(() -> LoggerFactory.getLogger("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Logger name cannot be empty");
    }

    @Test
    void testMultipleLoggerNames() {
        Logger logger1 = LoggerFactory.getLogger("org.flossware.test.a");
        Logger logger2 = LoggerFactory.getLogger("org.flossware.test.b");

        assertThat(logger1).isNotSameAs(logger2);
        assertThat(logger1.getName()).isEqualTo("org.flossware.test.a");
        assertThat(logger2.getName()).isEqualTo("org.flossware.test.b");
    }

    @Test
    void testLoggerNamesMatchClass() {
        String expectedName = this.getClass().getName();
        Logger logger = LoggerFactory.getLogger(this.getClass());

        assertThat(logger.getName()).isEqualTo(expectedName);
    }
}
