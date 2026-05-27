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

package org.flossware.netbeans.claude.documentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Extracts method signature information from source code
 */
public class MethodSignatureExtractor {

    private static final Pattern METHOD_PATTERN = Pattern.compile(
        "(?:public|private|protected)\\s+" +       // modifiers
        "(?:static\\s+)?" +                        // optional static
        "(?:final\\s+)?" +                         // optional final
        "(?:<[^>]+>\\s+)?" +                       // optional generic return
        "([\\w<>\\[\\]]+)\\s+" +                   // return type
        "(\\w+)\\s*\\(([^)]*)\\)" +                // method name and params
        "(?:\\s+throws\\s+([^{]+))?",             // optional throws
        Pattern.MULTILINE
    );

    /**
     * Extract method information from code
     */
    public static MethodInfo extract(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        Matcher matcher = METHOD_PATTERN.matcher(code);
        if (matcher.find()) {
            String returnType = matcher.group(1).trim();
            String name = matcher.group(2).trim();
            String paramsStr = matcher.group(3);
            String throwsStr = matcher.group(4);

            List<String> parameters = parseParameters(paramsStr);
            List<String> exceptions = parseExceptions(throwsStr);

            return new MethodInfo(name, returnType, parameters, exceptions);
        }

        return null;
    }

    /**
     * Parse parameter list
     */
    static List<String> parseParameters(String paramsStr) {
        if (paramsStr == null || paramsStr.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(paramsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Parse throws clause
     */
    static List<String> parseExceptions(String throwsStr) {
        if (throwsStr == null || throwsStr.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(throwsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
