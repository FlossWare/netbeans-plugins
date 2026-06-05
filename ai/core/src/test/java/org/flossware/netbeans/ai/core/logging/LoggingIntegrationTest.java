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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for logging configuration and usage patterns.
 *
 * <p>Verifies that the logging system is properly configured and that
 * loggers can emit messages at all levels as expected.</p>
 */
class LoggingIntegrationTest {

    private Logger testLogger;
    private TestLogHandler testHandler;

    @BeforeEach
    void setUp() {
        LoggerFactory.clearCache();
        testLogger = LoggerFactory.getLogger(LoggingIntegrationTest.class);
        testLogger.setLevel(Level.FINEST);  // Enable all log levels for testing
        testHandler = new TestLogHandler();
        testHandler.setLevel(Level.FINEST);  // Capture all log levels
        testLogger.addHandler(testHandler);
    }

    @Test
    void testLoggerCanEmitInfoMessages() {
        testLogger.info("Test info message");

        assertThat(testHandler.hasMessage("Test info message"))
            .as("Handler should capture info messages")
            .isTrue();
    }

    @Test
    void testLoggerCanEmitWarningMessages() {
        testLogger.warning("Test warning message");

        assertThat(testHandler.hasMessage("Test warning message"))
            .as("Handler should capture warning messages")
            .isTrue();
    }

    @Test
    void testLoggerCanEmitSevereMessages() {
        testLogger.severe("Test severe message");

        assertThat(testHandler.hasMessage("Test severe message"))
            .as("Handler should capture severe messages")
            .isTrue();
    }

    @Test
    void testLoggerCanEmitFineMessages() {
        testLogger.fine("Test fine message");

        assertThat(testHandler.hasMessage("Test fine message"))
            .as("Handler should capture fine messages")
            .isTrue();
    }

    @Test
    void testLoggerHandlesException() {
        RuntimeException exception = new RuntimeException("Test error");
        testLogger.log(java.util.logging.Level.SEVERE, "An error occurred", exception);

        assertThat(testHandler.hasMessage("An error occurred"))
            .as("Handler should capture message with exception")
            .isTrue();
    }

    @Test
    void testMultipleLoggersFromFactory() {
        Logger logger1 = LoggerFactory.getLogger(LoggingIntegrationTest.class);
        Logger logger2 = LoggerFactory.getLogger("org.flossware.custom.logger");

        assertThat(logger1).isNotNull();
        assertThat(logger2).isNotNull();
        assertThat(logger1.getName()).isEqualTo(LoggingIntegrationTest.class.getName());
        assertThat(logger2.getName()).isEqualTo("org.flossware.custom.logger");
    }

    @Test
    void testLogMessageFormatting() {
        String arg1 = "API";
        String arg2 = "v1/messages";
        testLogger.info("Sending request to " + arg1 + " endpoint: " + arg2);

        assertThat(testHandler.hasMessage("Sending request to API endpoint: v1/messages"))
            .as("Handler should capture formatted message")
            .isTrue();
    }

    @Test
    void testLoggerCacheEfficiency() {
        // Get the same logger multiple times
        Logger logger1 = LoggerFactory.getLogger(LoggingIntegrationTest.class);
        Logger logger2 = LoggerFactory.getLogger(LoggingIntegrationTest.class);
        Logger logger3 = LoggerFactory.getLogger(LoggingIntegrationTest.class);

        // All should be the same instance
        assertThat(logger1)
            .isSameAs(logger2)
            .isSameAs(logger3);
    }

    /**
     * Test handler that captures log records for assertion testing.
     */
    private static class TestLogHandler extends Handler {

        private final List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
            // No-op for test
        }

        @Override
        public void close() {
            records.clear();
        }

        /**
         * Checks if any logged message contains the specified text.
         *
         * @param text The text to search for
         * @return true if any message contains the text, false otherwise
         */
        boolean hasMessage(String text) {
            return records.stream()
                .anyMatch(record -> {
                    String message = record.getMessage();
                    return message != null && message.contains(text);
                });
        }
    }
}
