# Contributing to NetBeans AI Plugins

Thank you for your interest in contributing! This document provides guidelines for contributing to the project.

---

## Table of Contents

1. [Code of Conduct](#code-of-conduct)
2. [Getting Started](#getting-started)
3. [Development Setup](#development-setup)
4. [Contribution Workflow](#contribution-workflow)
5. [Coding Standards](#coding-standards)
6. [Testing Requirements](#testing-requirements)
7. [Adding New AI Providers](#adding-new-ai-providers)
8. [Adding New Languages](#adding-new-languages)
9. [Documentation](#documentation)
10. [Submitting Changes](#submitting-changes)

---

## Code of Conduct

This project adheres to the [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.

---

## Getting Started

### Prerequisites

- **Java**: JDK 11 or higher
- **Maven**: 3.6 or higher
- **NetBeans**: 22.0 or higher (RELEASE220)
- **Git**: For version control

### Fork and Clone

```bash
# Fork the repository on GitHub
# Then clone your fork
git clone https://github.com/YOUR_USERNAME/netbeans-plugins.git
cd netbeans-plugins
```

### Build the Project

```bash
mvn clean package
```

This builds all 23 modules and runs tests.

---

## Development Setup

### IDE Setup

**Recommended:** NetBeans IDE 22.0+

1. Open NetBeans
2. File → Open Project
3. Select the `netbeans-plugins` directory
4. NetBeans will recognize it as a multi-module Maven project

### Running Tests

```bash
# All tests
mvn clean test

# Specific module
mvn test -pl ai/claude

# With coverage report
mvn clean test jacoco:report
```

### Test Coverage

Current coverage target: **95%** (matches Claude module)

View coverage report:
```bash
open ai/claude/target/site/jacoco/index.html
```

---

## Contribution Workflow

### 1. Create a Branch

```bash
git checkout -b feature/your-feature-name
```

Branch naming:
- `feature/` - New features
- `fix/` - Bug fixes
- `docs/` - Documentation
- `test/` - Test improvements
- `refactor/` - Code refactoring

### 2. Make Changes

- Follow [coding standards](#coding-standards)
- Add tests for new functionality
- Update documentation as needed

### 3. Test Your Changes

```bash
# Build
mvn clean package

# Run tests
mvn test

# Check coverage
mvn clean test jacoco:report
```

### 4. Commit

```bash
git add .
git commit -m "Brief description

Detailed explanation of changes.
Fixes #issue-number (if applicable)"
```

Commit message guidelines:
- Use present tense ("Add feature" not "Added feature")
- First line: Brief summary (50 chars max)
- Blank line
- Detailed explanation (if needed)
- Reference issues: "Fixes #123" or "Relates to #456"

### 5. Push and Create PR

```bash
git push origin feature/your-feature-name
```

Then create a Pull Request on GitHub.

---

## Coding Standards

### Java Style

- **Indentation**: 4 spaces (no tabs)
- **Line length**: 120 characters max
- **Naming**:
  - Classes: `PascalCase`
  - Methods: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Variables: `camelCase`

### License Headers

All Java files must include GPL v3.0 license header:

```java
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
```

### Code Quality

- ✅ No `System.out.println()` - use proper logging
- ✅ No hardcoded credentials
- ✅ No TODO/FIXME in production code
- ✅ Proper exception handling
- ✅ Meaningful variable names
- ✅ Javadoc for public APIs

---

## Testing Requirements

### Test Coverage Target

**Minimum:** 60% line coverage (enforced by JaCoCo)  
**Target:** 95% line coverage (Claude module standard)

### Test Structure

```
src/test/java/
└── org/flossware/netbeans/{module}/
    ├── api/
    │   ├── {Module}ClientTest.java
    │   └── {Module}ServiceTest.java
    ├── ui/
    │   └── {Module}WindowTopComponentTest.java
    ├── actions/
    │   └── Ask{Module}ActionTest.java
    ├── context/
    │   └── ProjectContextTest.java
    └── options/
        ├── {Module}OptionsPanelTest.java
        └── {Module}OptionsPanelControllerTest.java
```

### Test Frameworks

- **JUnit 5** - Test framework
- **Mockito** - Mocking
- **AssertJ** - Fluent assertions
- **MockWebServer** - HTTP testing

### Example Test

```java
@ExtendWith(MockitoExtension.class)
class MyServiceTest {

    @Mock
    private MyClient mockClient;

    private MyService service;

    @BeforeEach
    void setUp() {
        service = MyService.getInstance();
        // Inject mock via reflection
    }

    @Test
    void testSomething() {
        // Arrange
        when(mockClient.doSomething()).thenReturn("result");

        // Act
        String actual = service.doSomething();

        // Assert
        assertThat(actual).isEqualTo("result");
        verify(mockClient).doSomething();
    }
}
```

---

## Adding New AI Providers

### 1. Create Module Structure

```bash
mkdir -p ai/{provider}/src/main/java/org/flossware/netbeans/{provider}/{api,ui,actions,context,options}
mkdir -p ai/{provider}/src/main/resources/org/flossware/netbeans/{provider}/{ui,resources}
mkdir -p ai/{provider}/src/test/java/org/flossware/netbeans/{provider}/{api,ui,actions,context,options}
```

### 2. Copy Template

Use Mistral as template (simplest HTTP-based implementation):

```bash
# Copy structure from Mistral
cp -r ai/mistral/* ai/{provider}/
# Then rename all occurrences of "Mistral" to your provider name
```

### 3. Required Files

**API Layer:**
- `{Provider}Client.java` - HTTP client (OkHttp + Gson)
- `{Provider}Service.java` - Singleton service with async methods

**UI Layer:**
- `{Provider}WindowTopComponent.java` - Chat window
- `{Provider}WindowTopComponent.form` - UI form

**Actions:**
- `Ask{Provider}Action.java` - Context menu action

**Context:**
- `ProjectContext.java` - Extract project context

**Options:**
- `{Provider}OptionsPanel.java` - Settings panel
- `{Provider}OptionsPanelController.java` - Controller

**Resources:**
- `Bundle.properties` - Internationalization
- `{provider}-icon.png` - 16x16 icon

**Tests:**
- All classes above need corresponding test files

### 4. Update Parent POM

Add module to `pom.xml`:

```xml
<modules>
    <!-- ... existing modules ... -->
    <module>ai/{provider}</module>
</modules>
```

### 5. Update Documentation

- Add to README.md AI providers list
- Create `ai/{provider}/README.md`
- Add API key signup link
- Document FREE tier (if available)

### 6. Test Coverage

Achieve 95% test coverage before submitting PR.

---

## Adding New Languages

### 1. Language Requirements

- Must have syntax definition
- Ideally has LSP (Language Server Protocol) server
- Community demand/use case

### 2. Create Module

```bash
mkdir -p languages/{language}/src/main/java/org/flossware/netbeans/{language}
```

### 3. Required Components

**Core:**
- Lexer - Token recognition
- Parser - Syntax tree
- Language definition

**Features:**
- Syntax highlighting
- Code completion
- Error detection
- Debugging (if applicable)

**Use Common Module:**
```xml
<dependency>
    <groupId>org.flossware.netbeans</groupId>
    <artifactId>netbeans-common</artifactId>
</dependency>
```

### 4. Update Parent POM

```xml
<modules>
    <!-- ... existing modules ... -->
    <module>languages/{language}</module>
</modules>
```

---

## Documentation

### Required Documentation

When adding features:

1. **Code Comments**
   - Javadoc for public APIs
   - Complex logic explained
   - No obvious comments

2. **README Updates**
   - Update main README.md
   - Update module-specific README
   - Update PLUGINS_SUMMARY.md

3. **SCREENSHOTS.md**
   - Add visual examples
   - Show new features in action

4. **CHANGELOG.md** (when created)
   - Document changes
   - Note breaking changes

---

## Submitting Changes

### Pull Request Checklist

Before submitting PR:

- [ ] Code follows style guidelines
- [ ] All tests pass (`mvn test`)
- [ ] Coverage meets target (95% or 60% minimum)
- [ ] Documentation updated
- [ ] License headers present
- [ ] No debug code (`System.out`, TODO, etc.)
- [ ] Commit messages are clear
- [ ] PR description explains changes

### PR Template

```markdown
## Description
Brief description of changes

## Motivation
Why is this change needed?

## Changes Made
- Added feature X
- Fixed bug Y
- Updated documentation Z

## Testing
How was this tested?

## Related Issues
Fixes #123
Relates to #456

## Checklist
- [ ] Tests added/updated
- [ ] Documentation updated
- [ ] Coverage target met
```

### Review Process

1. Submit PR
2. CI/CD runs automatically
3. Maintainer reviews code
4. Address feedback
5. PR merged when approved

---

## Questions?

- **Issues**: https://github.com/FlossWare/netbeans-plugins/issues
- **Discussions**: GitHub Discussions (if enabled)
- **Email**: (maintainer email)

---

**Thank you for contributing!**

Your contributions help make NetBeans better for everyone.

