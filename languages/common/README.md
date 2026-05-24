# NetBeans Language Support Common Module

Shared abstractions and utilities for building LSP-based language support plugins for NetBeans.

## Overview

This module provides reusable base classes and interfaces that simplify the creation of language support plugins using the Language Server Protocol (LSP). It eliminates code duplication and provides a consistent architecture across language plugins.

## Features

- **LSP Server Management**: Abstract launcher for LSP servers
- **Code Completion**: Abstract completion providers, queries, and items
- **Project Management**: Abstract project factory and project implementation
- **Execution Support**: Abstract run actions for language execution
- **UI Components**: Abstract console component for interactive shells
- **Settings Management**: Abstract settings for plugin configuration

## Requirements

- NetBeans 22.0 or later (RELEASE220+)
- NetBeans LSP client support
- JDK 11 or later

## Architecture

### LSP Integration

#### AbstractLspServerLauncher
Base class for launching and managing LSP server processes.

**Extend this to**:
- Start language-specific LSP servers
- Configure server startup parameters
- Handle server lifecycle

**Example**:
```java
public class PythonLspServerLauncher extends AbstractLspServerLauncher {
    @Override
    protected String getServerCommand() {
        return "pylsp";
    }
    
    @Override
    protected List<String> getServerArgs() {
        return Arrays.asList("--verbose");
    }
}
```

### Code Completion

#### AbstractLspCompletionProvider
Registers the completion provider with NetBeans.

#### AbstractLspCompletionQuery
Queries the LSP server for completion suggestions.

#### AbstractLspCompletionItem
Represents individual completion items from the LSP server.

**Example**:
```java
public class PythonLspCompletionProvider extends AbstractLspCompletionProvider {
    @Override
    protected String getMimeType() {
        return "text/x-python";
    }
}
```

### Project Management

#### AbstractProjectFactory
Recognizes and creates project instances for specific file types.

**Extend this to**:
- Detect language-specific project markers
- Create project instances

**Example**:
```java
public class PythonProjectFactory extends AbstractProjectFactory {
    @Override
    protected boolean isProjectDirectory(FileObject dir) {
        return dir.getFileObject("setup.py") != null ||
               dir.getFileObject("pyproject.toml") != null;
    }
}
```

#### AbstractProject
Represents a language-specific project.

### Execution

#### AbstractRunAction
Provides base functionality for running code in a language.

**Extend this to**:
- Execute language-specific code
- Capture and display output
- Handle runtime errors

### UI Components

#### AbstractConsoleTopComponent
Provides an interactive console/REPL interface.

**Extend this to**:
- Create language-specific REPLs
- Handle user input
- Display execution results

### Settings

#### AbstractSettings
Manages plugin configuration and preferences.

**Extend this to**:
- Store language-specific settings
- Provide default values
- Validate configuration

## Usage

### Creating a New Language Plugin

1. **Add dependency** in your plugin's `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.flossware.netbeans</groupId>
       <artifactId>netbeans-common</artifactId>
       <version>${project.version}</version>
   </dependency>
   ```

2. **Extend the abstractions**:
   - LSP Server: Extend `AbstractLspServerLauncher`
   - Completion: Extend `AbstractLspCompletionProvider`, `AbstractLspCompletionQuery`, `AbstractLspCompletionItem`
   - Projects: Extend `AbstractProjectFactory` and `AbstractProject`
   - Execution: Extend `AbstractRunAction`
   - Console: Extend `AbstractConsoleTopComponent`
   - Settings: Extend `AbstractSettings`

3. **Register in `layer.xml`**:
   ```xml
   <folder name="Services">
       <file name="YourProjectFactory.instance">
           <attr name="instanceCreate" 
                 methodvalue="org.yourpackage.YourProjectFactory.create"/>
       </file>
   </folder>
   ```

4. **Configure in `Bundle.properties`**:
   ```properties
   your.language.name=Your Language
   your.language.mime.type=text/x-your-language
   ```

## Design Principles

### Single Responsibility
Each abstract class has a single, well-defined responsibility.

### Template Method Pattern
Abstract classes define the algorithm structure; subclasses provide specific implementations.

### Dependency Injection
Dependencies are provided through constructor parameters or factory methods.

### Minimal Surface Area
Abstract classes expose only the methods that subclasses need to override.

## Testing

### Unit Tests
The module includes unit tests for all abstract classes:
```bash
mvn clean test
```

### Test Utilities
Common test utilities are provided for language plugin developers.

## Examples

See the following implementations:
- **Python**: `languages/python/` - Full LSP-based Python support

## Module Structure

```
languages/common/
├── pom.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/flossware/netbeans/common/
│   │           ├── completion/        # Code completion
│   │           │   ├── AbstractLspCompletionItem.java
│   │           │   ├── AbstractLspCompletionProvider.java
│   │           │   └── AbstractLspCompletionQuery.java
│   │           ├── execution/         # Code execution
│   │           │   └── AbstractRunAction.java
│   │           ├── lsp/              # LSP integration
│   │           │   └── AbstractLspServerLauncher.java
│   │           ├── project/          # Project management
│   │           │   ├── AbstractProject.java
│   │           │   └── AbstractProjectFactory.java
│   │           ├── settings/         # Configuration
│   │           │   └── AbstractSettings.java
│   │           └── ui/               # UI components
│   │               └── AbstractConsoleTopComponent.java
│   └── test/                         # Unit tests
│       └── java/
│           └── org/flossware/netbeans/common/
│               ├── lsp/
│               │   └── AbstractLspServerLauncherTest.java
│               ├── project/
│               │   └── AbstractProjectFactoryTest.java
│               └── settings/
│                   └── AbstractSettingsTest.java
└── README.md
```

## Roadmap

Future enhancements:
- [ ] Debug protocol support (DAP)
- [ ] Syntax highlighting abstractions
- [ ] Code formatting abstractions
- [ ] Refactoring support
- [ ] Test runner integration

## License

See LICENSE file in the project root.

## Related Modules

- `languages/python`: Python language support implementation
- `ai/claude`: Claude AI integration (shares common patterns)
- `ai/gemini`: Gemini AI integration
- `ai/chatgpt`: ChatGPT AI integration

## Contributing

When adding new abstractions:
1. Follow existing patterns
2. Add unit tests
3. Update this README
4. Provide example usage
5. Consider backward compatibility

## Support

For issues or questions:
- GitHub Issues: https://github.com/FlossWare/netbeans-plugins/issues
- Documentation: This README and inline JavaDoc
- Examples: See `languages/python/` module
