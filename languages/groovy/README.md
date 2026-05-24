# NetBeans Groovy Language Support Plugin

LSP-based Groovy language support for NetBeans IDE.

## Features

- **Code Completion**: Intelligent code completion via Groovy LSP
- **Project Management**: Groovy project recognition (Gradle, Maven)
- **Code Execution**: Run Groovy scripts from within NetBeans
- **Console Integration**: Interactive Groovy shell (groovysh)
- **LSP Integration**: Full Language Server Protocol support

## Requirements

### Minimum NetBeans Version
- **NetBeans 22.0 or later** (RELEASE220+)
- Required for LSP client support (`org.netbeans.modules.lsp.client`)

### External Dependencies
- **Groovy**: Groovy 3.0 or later installed on your system
- **Groovy Language Server**: groovy-language-server installed
  ```bash
  npm install -g groovy-language-server
  ```

### Runtime Dependencies
This plugin depends on NetBeans' built-in LSP client module, which is:
- Available at runtime in NetBeans 22.0+
- NOT available as a Maven Central dependency
- Causes compilation warnings but works correctly when deployed

## Installation

### From NBM File
1. Build the plugin: `mvn clean package`
2. In NetBeans: Tools → Plugins → Downloaded
3. Click "Add Plugins" and select `target/netbeans-groovy-*.nbm`
4. Install and restart NetBeans

### Development Mode
1. Open project in NetBeans IDE
2. Right-click project → Run
3. NetBeans creates temporary module for testing

## Configuration

### Groovy Interpreter Path
1. Tools → Options → Groovy
2. Set path to `groovy` executable
3. Default: Auto-detected from system PATH

### Groovy LSP Server Path
1. Tools → Options → Groovy
2. Set path to `groovy-language-server` executable
3. Default: Auto-detected from system PATH

## Build Notes

### LSP Client Dependency
The `org.netbeans.modules.lsp.client` dependency is commented out in `pom.xml` because:
- It's not available in Maven Central
- It's provided at runtime by NetBeans 22.0+
- The plugin will compile with warnings but work correctly when installed

### Build Warnings
You may see warnings like:
```
package org.netbeans.modules.lsp.client does not exist
```
These are expected and do not affect runtime functionality.

## Project Detection

The plugin recognizes Groovy projects based on:
- **Gradle**: Presence of `build.gradle` file
- **Maven**: Presence of `pom.xml` file
- **Plain Groovy**: Directories containing `.groovy` files

## Supported File Extensions

- `.groovy` - Standard Groovy files
- `.gvy` - Groovy script files
- `.gy` - Groovy files
- `.gsh` - Groovy shell scripts

## Features

### Code Completion
Triggered automatically when typing:
- `.` (dot) - Member access
- `@` (at) - Annotation completion

Provides:
- Method and property suggestions
- Class and import suggestions
- Keyword completion
- Context-aware suggestions

### Running Groovy Scripts
1. Right-click on a `.groovy` file
2. Select "Run Groovy Script"
3. Output appears in the NetBeans Output window

### Interactive Console
1. Window → Groovy Console
2. Type Groovy expressions
3. Press Enter to evaluate
4. Uses `groovysh` for interactive execution

## Architecture

### LSP Integration
- `GroovyLspServerLauncher`: Manages Groovy LSP server process
- Extends `AbstractLspServerLauncher` from common module

### Code Completion
- `GroovyLspCompletionProvider`: Registers completion provider
- `GroovyLspCompletionQuery`: Queries LSP for completions
- `GroovyLspCompletionItem`: Individual completion items

### Project Management
- `GroovyProjectFactory`: Recognizes Groovy projects
- `GroovyProject`: Groovy project implementation

### Execution
- `RunGroovyAction`: Execute Groovy scripts
- `GroovyConsoleTopComponent`: Interactive shell

## Testing

### Unit Tests
```bash
mvn clean test
```

### Integration Tests
```bash
mvn clean verify
```

Integration tests require:
- Groovy installed
- Groovy language server installed
- NetBeans runtime environment

## Troubleshooting

### LSP Server Not Found
**Error**: "Groovy language server not found"  
**Solution**: Install groovy-language-server:
```bash
npm install -g groovy-language-server
```

Or build from source:
```bash
git clone https://github.com/GroovyLanguageServer/groovy-language-server
cd groovy-language-server
./gradlew build
```

### No Code Completion
**Error**: Code completion not working  
**Solutions**:
1. Verify Groovy language server is installed
2. Check Groovy path in Tools → Options → Groovy
3. Check NetBeans version (22.0+ required)
4. View log: View → IDE Log for LSP errors

### ClassNotFoundException at Runtime
**Error**: `org.netbeans.modules.lsp.client.* not found`  
**Solution**: Ensure NetBeans 22.0 or later is being used

### Groovy Not Found
**Error**: "groovy command not found"  
**Solution**: 
1. Install Groovy: https://groovy-lang.org/install.html
2. Ensure `groovy` is in system PATH
3. Or set custom path in Tools → Options → Groovy

## Development

### Adding Features
1. Extend abstractions from `languages/common`
2. Implement Groovy-specific behavior
3. Register in `layer.xml`

### Module Structure
```
languages/groovy/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/flossware/netbeans/groovy/
│   │   │       ├── completion/     # Code completion
│   │   │       ├── execution/      # Groovy execution
│   │   │       ├── lsp/           # LSP integration
│   │   │       ├── project/       # Project management
│   │   │       ├── settings/      # Configuration
│   │   │       └── ui/            # Console UI
│   │   └── resources/
│   │       └── org/flossware/netbeans/groovy/
│   │           ├── Bundle.properties
│   │           ├── GroovyResolver.xml
│   │           └── layer.xml
│   ├── test/                      # Unit tests
│   └── integration-test/          # Integration tests
└── README.md
```

## License

See LICENSE file in the project root.

## Related Modules

- `languages/common`: Shared LSP abstractions
- `languages/python`: Python language support
- `languages/beanshell`: BeanShell language support
- `languages/mvel`: MVEL language support
- `ai/claude`: Claude AI integration plugin
- `ai/gemini`: Gemini AI integration plugin
- `ai/chatgpt`: ChatGPT AI integration plugin

## Resources

- **Groovy**: https://groovy-lang.org/
- **Groovy Language Server**: https://github.com/GroovyLanguageServer/groovy-language-server
- **NetBeans Platform**: https://netbeans.apache.org/

## Support

For issues or questions:
- GitHub Issues: https://github.com/FlossWare/netbeans-plugins/issues
- Check IDE Log: View → IDE Log
- Enable LSP logging for detailed diagnostics
