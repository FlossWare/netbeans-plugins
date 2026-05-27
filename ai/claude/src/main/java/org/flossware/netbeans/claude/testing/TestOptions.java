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

/**
 * Configuration options for test generation
 */
public class TestOptions {

    private String framework = "JUnit 5";
    private String mockingLibrary = "Mockito";
    private String assertionLibrary = "AssertJ";
    private int coverageTarget = 95;
    private boolean includeEdgeCases = true;
    private boolean includeErrorHandling = true;

    public TestOptions() {
    }

    public String getFramework() {
        return framework;
    }

    public void setFramework(String framework) {
        this.framework = framework;
    }

    public String getMockingLibrary() {
        return mockingLibrary;
    }

    public void setMockingLibrary(String mockingLibrary) {
        this.mockingLibrary = mockingLibrary;
    }

    public String getAssertionLibrary() {
        return assertionLibrary;
    }

    public void setAssertionLibrary(String assertionLibrary) {
        this.assertionLibrary = assertionLibrary;
    }

    public int getCoverageTarget() {
        return coverageTarget;
    }

    public void setCoverageTarget(int coverageTarget) {
        this.coverageTarget = coverageTarget;
    }

    public boolean isIncludeEdgeCases() {
        return includeEdgeCases;
    }

    public void setIncludeEdgeCases(boolean includeEdgeCases) {
        this.includeEdgeCases = includeEdgeCases;
    }

    public boolean isIncludeErrorHandling() {
        return includeErrorHandling;
    }

    public void setIncludeErrorHandling(boolean includeErrorHandling) {
        this.includeErrorHandling = includeErrorHandling;
    }
}
