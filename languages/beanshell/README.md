# BeanShell Language Support for NetBeans

This NetBeans plugin provides BeanShell language support including project management, script execution, and an interactive console.

## Features

### Available Features

- **Script Execution**: Run BeanShell scripts directly from NetBeans
  - Right-click on `.bsh` or `.beanshell` files and select "Run BeanShell Script"
  - Output appears in the NetBeans output window
  
- **Interactive Console**: REPL (Read-Eval-Print Loop) for BeanShell
  - Access via Window → BeanShell Console
  - Execute BeanShell code snippets and see immediate results
  
- **Project Management**: Basic project support for BeanShell projects
  - Recognizes directories with `.bsh` or `.beanshell` files
  - Supports Gradle and Maven projects with BeanShell
  
- **File Recognition**: Automatic detection of BeanShell files
  - `.bsh` files
  - `.beanshell` files

### LSP Support Status

**Important Note**: BeanShell does not currently have a standard Language Server Protocol (LSP) implementation available. As a result, LSP-based features are not available:

- ❌ Code completion
- ❌ Syntax highlighting (beyond basic text editor)
- ❌ Error checking
- ❌ Code navigation (go to definition, find references)
- ❌ Refactoring support

If a BeanShell LSP server becomes available in the future, this plugin can be updated to integrate it.

## Requirements

- NetBeans 22.0 or later
- BeanShell interpreter (`bsh`) installed and available in your PATH
  - Download from: http://www.beanshell.org/
  - Or install via package manager (e.g., `apt install bsh` on Debian/Ubuntu)

## Installation

1. Build the plugin:
   ```bash
   mvn clean package
   ```

2. Install the NBM file in NetBeans:
   - Go to Tools → Plugins → Downloaded
   - Click "Add Plugins..."
   - Select the `.nbm` file from `target/`
   - Click "Install"

## Configuration

### BeanShell Interpreter Path

By default, the plugin uses `bsh` from your system PATH. To configure a custom path:

1. The settings can be adjusted programmatically through `BeanShellSettings.getInstance()`
2. Set the BeanShell interpreter path: `setBeanShellPath("/path/to/bsh")`

## Usage

### Running BeanShell Scripts

1. Create or open a `.bsh` file
2. Right-click in the editor or on the file
3. Select "Run BeanShell Script"
4. View output in the NetBeans output window

### Using the Interactive Console

1. Open the console: Window → BeanShell Console
2. Type BeanShell expressions and press Enter
3. Results are displayed immediately

### Example BeanShell Script

```java
// hello.bsh
print("Hello from BeanShell!");

// Variables and expressions
int x = 10;
int y = 20;
print("x + y = " + (x + y));

// Java interop
import java.util.*;
List list = new ArrayList();
list.add("Item 1");
list.add("Item 2");
print("List: " + list);
```

## Project Structure

```
beanshell/
├── src/main/java/org/flossware/netbeans/beanshell/
│   ├── completion/          # LSP completion (inactive - no LSP server)
│   ├── execution/           # Script execution support
│   ├── lsp/                 # LSP server launcher (returns null)
│   ├── project/             # Project factory and management
│   ├── settings/            # Plugin settings
│   └── ui/                  # Interactive console
├── src/main/resources/org/flossware/netbeans/beanshell/
│   ├── Bundle.properties    # Localization
│   ├── layer.xml           # NetBeans layer registration
│   └── BeanShellResolver.xml # MIME type resolver
└── src/test/java/          # Unit tests
```

## Architecture

This plugin follows the common architecture pattern used across all language plugins in this project:

- Extends `AbstractRunAction` for script execution
- Extends `AbstractConsoleTopComponent` for the interactive console
- Extends `AbstractProject` and `AbstractProjectFactory` for project support
- Extends `AbstractSettings` for configuration management
- Extends `AbstractLspServerLauncher` (returns null as no LSP server exists)

## Future Enhancements

If BeanShell LSP support becomes available:

1. Update `BeanShellLspServerLauncher.getServerCommands()` to return the LSP server command
2. Update `BeanShellLspServerLauncher.getLaunchArgs()` with appropriate launch arguments
3. The completion provider will automatically activate
4. Code completion and other LSP features will become available

## Contributing

Contributions are welcome! Please see the main project README for contribution guidelines.

## License

This plugin is part of the FlossWare NetBeans Plugins project. See the main project LICENSE for details.

## Links

- BeanShell: http://www.beanshell.org/
- BeanShell GitHub: https://github.com/beanshell/beanshell
- NetBeans Platform: https://netbeans.apache.org/

## Support

For issues, questions, or contributions, please visit the main project repository.
