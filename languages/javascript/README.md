# NetBeans JavaScript Language Support Plugin

LSP-based JavaScript language support for NetBeans IDE.

## Features

- **Code Completion**: Intelligent code completion via JavaScript LSP
- **Project Management**: JavaScript project recognition and management
- **Code Execution**: Run JavaScript scripts from within NetBeans
- **Console Integration**: Interactive JavaScript console (Node.js REPL)
- **LSP Integration**: Full Language Server Protocol support

## Requirements

### Minimum NetBeans Version
- **NetBeans 22.0 or later** (RELEASE220+)
- Required for LSP client support (`org.netbeans.modules.lsp.client`)

### External Dependencies
- **Node.js**: Node.js 16.0 or later installed on your system
- **JavaScript LSP Server**: `typescript-language-server` installed (supports both TypeScript and JavaScript)
  ```bash
  npm install -g typescript-language-server typescript
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
3. Click "Add Plugins" and select `target/netbeans-javascript-*.nbm`
4. Install and restart NetBeans

### Development Mode
1. Open project in NetBeans IDE
2. Right-click project → Run
3. NetBeans creates temporary module for testing

## Configuration

### JavaScript LSP Server Path
1. Tools → Options → JavaScript
2. Set path to `typescript-language-server` executable
3. Default: Auto-detected from system PATH

### JavaScript Runtime
1. Tools → Options → JavaScript
2. Set path to Node.js interpreter
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

## Architecture

### LSP Integration
- `JavaScriptLspServerLauncher`: Manages JavaScript LSP server process
- Extends `AbstractLspServerLauncher` from common module

### Code Completion
- `JavaScriptLspCompletionProvider`: Registers completion provider
- `JavaScriptLspCompletionQuery`: Queries LSP for completions
- `JavaScriptLspCompletionItem`: Individual completion items

### Project Management
- `JavaScriptProjectFactory`: Recognizes JavaScript projects
- `JavaScriptProject`: JavaScript project implementation

### Execution
- `RunJavaScriptAction`: Execute JavaScript scripts
- `JavaScriptConsoleTopComponent`: Interactive console

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
- Node.js installed
- JavaScript LSP server installed
- NetBeans runtime environment

## Troubleshooting

### LSP Server Not Found
**Error**: "JavaScript LSP server not found"
**Solution**: Install typescript-language-server:
```bash
npm install -g typescript-language-server typescript
```

### No Code Completion
**Error**: Code completion not working
**Solutions**:
1. Verify JavaScript LSP server is installed
2. Check Node.js path in Tools → Options → JavaScript
3. Check NetBeans version (22.0+ required)
4. View log: View → IDE Log for LSP errors

### ClassNotFoundException at Runtime
**Error**: `org.netbeans.modules.lsp.client.* not found`
**Solution**: Ensure NetBeans 22.0 or later is being used

## Development

### Adding LSP Features
1. Extend abstractions from `languages/common`
2. Implement JavaScript-specific behavior
3. Register in `layer.xml`

### Module Structure
```
languages/javascript/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/flossware/netbeans/javascript/
│   │   │       ├── completion/     # Code completion
│   │   │       ├── debugger/       # Debugging support
│   │   │       ├── execution/      # JavaScript execution
│   │   │       ├── lsp/           # LSP integration
│   │   │       ├── project/       # Project management
│   │   │       ├── settings/      # Configuration
│   │   │       └── ui/            # Console UI
│   │   └── resources/
│   │       └── org/flossware/netbeans/javascript/
│   │           ├── Bundle.properties
│   │           ├── JavaScriptResolver.xml
│   │           └── layer.xml
│   ├── test/                      # Unit tests
│   └── integration-test/          # Integration tests
└── README.md
```

## Debugging

The plugin includes JavaScript debugging support via Node.js inspect protocol:

1. Right-click on a `.js` file → Debug JavaScript Script
2. The debugger starts on port 9229
3. Attach using:
   - Chrome DevTools (chrome://inspect)
   - VS Code debugger
   - Any DAP-compatible debugger

## License

See LICENSE file in the root directory (GPL v3.0).

## Related Modules

- `languages/common`: Shared LSP abstractions
- `languages/typescript`: TypeScript language support
- `ai/claude`: Claude AI integration plugin
- `ai/gemini`: Gemini AI integration plugin
- `ai/chatgpt`: ChatGPT AI integration plugin

## Support

For issues or questions:
- GitHub Issues: https://github.com/FlossWare/netbeans-plugins/issues
- Check IDE Log: View → IDE Log
- Enable LSP logging for detailed diagnostics
