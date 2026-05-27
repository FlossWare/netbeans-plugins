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

import java.util.List;

/**
 * Represents parsed stack trace information
 */
public class StackTraceInfo {

    private final String exceptionType;
    private final String message;
    private final String file;
    private final int line;
    private final List<String> stackTrace;

    public StackTraceInfo(String exceptionType, String message, String file, int line, List<String> stackTrace) {
        this.exceptionType = exceptionType;
        this.message = message;
        this.file = file;
        this.line = line;
        this.stackTrace = stackTrace;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public String getMessage() {
        return message;
    }

    public String getFile() {
        return file;
    }

    public int getLine() {
        return line;
    }

    public List<String> getStackTrace() {
        return stackTrace;
    }

    public boolean hasLocation() {
        return file != null && !file.isEmpty() && line > 0;
    }
}
