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

package org.flossware.netbeans.claude.debugging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parses Java stack traces to extract error information
 */
public class StackTraceParser {

    private static final Pattern EXCEPTION_PATTERN = Pattern.compile(
        "^([\\w.$]+Exception|[\\w.$]+Error):\\s*(.*)$",
        Pattern.MULTILINE
    );

    private static final Pattern LOCATION_PATTERN = Pattern.compile(
        "at\\s+[\\w.$]+\\.\\w+\\(([\\w.]+):(\\d+)\\)"
    );

    /**
     * Parse stack trace text
     */
    public static StackTraceInfo parse(String stackTrace) {
        if (stackTrace == null || stackTrace.trim().isEmpty()) {
            return new StackTraceInfo("Unknown", "No error message", null, 0, new ArrayList<>());
        }

        String exceptionType = extractExceptionType(stackTrace);
        String message = extractMessage(stackTrace);
        String file = extractFile(stackTrace);
        int line = extractLine(stackTrace);
        List<String> trace = extractStackTrace(stackTrace);

        return new StackTraceInfo(exceptionType, message, file, line, trace);
    }

    /**
     * Extract exception type (e.g., NullPointerException)
     */
    static String extractExceptionType(String stackTrace) {
        Matcher matcher = EXCEPTION_PATTERN.matcher(stackTrace);
        if (matcher.find()) {
            String fullType = matcher.group(1);
            int lastDot = fullType.lastIndexOf('.');
            return lastDot >= 0 ? fullType.substring(lastDot + 1) : fullType;
        }
        return "Unknown";
    }

    /**
     * Extract error message
     */
    static String extractMessage(String stackTrace) {
        Matcher matcher = EXCEPTION_PATTERN.matcher(stackTrace);
        if (matcher.find()) {
            String message = matcher.group(2).trim();
            return message.isEmpty() ? "No message provided" : message;
        }
        return "No error message";
    }

    /**
     * Extract file name from first stack trace line
     */
    static String extractFile(String stackTrace) {
        Matcher matcher = LOCATION_PATTERN.matcher(stackTrace);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Extract line number from first stack trace line
     */
    static int extractLine(String stackTrace) {
        Matcher matcher = LOCATION_PATTERN.matcher(stackTrace);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(2));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * Extract full stack trace as list of lines
     */
    static List<String> extractStackTrace(String stackTrace) {
        return Arrays.stream(stackTrace.split("\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
    }
}
