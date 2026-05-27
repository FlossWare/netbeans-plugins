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

import java.util.List;

/**
 * Represents method signature information
 */
public class MethodInfo {

    private final String name;
    private final String returnType;
    private final List<String> parameters;
    private final List<String> exceptions;

    public MethodInfo(String name, String returnType, List<String> parameters, List<String> exceptions) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    public boolean hasExceptions() {
        return exceptions != null && !exceptions.isEmpty();
    }
}
