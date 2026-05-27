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

package org.flossware.netbeans.claude.testing;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyzes source code to extract information for test generation
 */
public class CodeAnalyzer {

    private static final Pattern CLASS_PATTERN = Pattern.compile(
        "(?:public|private|protected)?\\s*(?:abstract|final)?\\s*class\\s+(\\w+)",
        Pattern.MULTILINE
    );

    private static final Pattern METHOD_PATTERN = Pattern.compile(
        "(?:public|private|protected)\\s+(?:static\\s+)?(?:\\w+(?:<[^>]+>)?(?:\\[\\])?\\s+)(\\w+)\\s*\\([^)]*\\)",
        Pattern.MULTILINE
    );

    private static final Pattern FIELD_PATTERN = Pattern.compile(
        "(?:private|protected|public)\\s+(?:static\\s+)?(?:final\\s+)?(?:\\w+(?:<[^>]+>)?(?:\\[\\])?\\s+)(\\w+)\\s*[;=]",
        Pattern.MULTILINE
    );

    private static final Pattern IMPORT_PATTERN = Pattern.compile(
        "import\\s+([\\w.]+);",
        Pattern.MULTILINE
    );

    /**
     * Analyze source code and extract structure
     */
    public static CodeAnalysis analyze(String sourceCode) {
        if (sourceCode == null || sourceCode.trim().isEmpty()) {
            return new CodeAnalysis(sourceCode, "Unknown", new ArrayList<>(),
                                  new ArrayList<>(), new ArrayList<>());
        }

        String className = extractClassName(sourceCode);
        List<String> methods = extractMethods(sourceCode);
        List<String> fields = extractFields(sourceCode);
        List<String> dependencies = extractDependencies(sourceCode);

        return new CodeAnalysis(sourceCode, className, methods, fields, dependencies);
    }

    /**
     * Extract class name from source code
     */
    static String extractClassName(String code) {
        Matcher matcher = CLASS_PATTERN.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }

    /**
     * Extract method names from source code
     */
    static List<String> extractMethods(String code) {
        List<String> methods = new ArrayList<>();
        Matcher matcher = METHOD_PATTERN.matcher(code);

        while (matcher.find()) {
            String methodName = matcher.group(1);
            if (!methods.contains(methodName)) {
                methods.add(methodName);
            }
        }

        return methods;
    }

    /**
     * Extract field names from source code
     */
    static List<String> extractFields(String code) {
        List<String> fields = new ArrayList<>();
        Matcher matcher = FIELD_PATTERN.matcher(code);

        while (matcher.find()) {
            String fieldName = matcher.group(1);
            if (!fields.contains(fieldName)) {
                fields.add(fieldName);
            }
        }

        return fields;
    }

    /**
     * Extract dependencies (imports) from source code
     */
    static List<String> extractDependencies(String code) {
        List<String> dependencies = new ArrayList<>();
        Matcher matcher = IMPORT_PATTERN.matcher(code);

        while (matcher.find()) {
            String importPath = matcher.group(1);
            if (!dependencies.contains(importPath)) {
                dependencies.add(importPath);
            }
        }

        return dependencies;
    }
}
