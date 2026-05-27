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

import java.util.List;

/**
 * Result of code analysis for test generation
 */
public class CodeAnalysis {

    private final String sourceCode;
    private final String className;
    private final List<String> methods;
    private final List<String> fields;
    private final List<String> dependencies;

    public CodeAnalysis(String sourceCode, String className, List<String> methods,
                       List<String> fields, List<String> dependencies) {
        this.sourceCode = sourceCode;
        this.className = className;
        this.methods = methods;
        this.fields = fields;
        this.dependencies = dependencies;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public String getClassName() {
        return className;
    }

    public List<String> getMethods() {
        return methods;
    }

    public List<String> getFields() {
        return fields;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public String getMethodNames() {
        return String.join(", ", methods);
    }
}
