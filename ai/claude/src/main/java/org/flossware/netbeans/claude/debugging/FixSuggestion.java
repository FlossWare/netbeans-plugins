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

/**
 * Represents a suggested fix for an error
 */
public class FixSuggestion {

    private final String description;
    private final String fixCode;
    private final int confidence;

    public FixSuggestion(String description, String fixCode, int confidence) {
        this.description = description;
        this.fixCode = fixCode;
        this.confidence = Math.max(0, Math.min(100, confidence));
    }

    public String getDescription() {
        return description;
    }

    public String getFixCode() {
        return fixCode;
    }

    public int getConfidence() {
        return confidence;
    }

    public boolean hasCode() {
        return fixCode != null && !fixCode.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s (confidence: %d%%)", description, confidence);
    }
}
