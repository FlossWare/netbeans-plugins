# NetBeans Go Language Support Plugin

LSP-based Go language support for NetBeans IDE.

## Features

- **Code Completion**: Intelligent code completion via Go LSP (gopls)
- **Project Management**: Go project recognition and management
- **Code Execution**: Run Go programs from within NetBeans
- **Console Integration**: Interactive Go console (REPL)
- **LSP Integration**: Full Language Server Protocol support

## Requirements

### Minimum NetBeans Version
- **NetBeans 22.0 or later** (RELEASE220+)
- Required for LSP client support (`org.netbeans.modules.lsp.client`)

### External Dependencies
- **Go**: Go 1.21 or later installed on your system
- **Go LSP Server**: `gopls` installed
  ```bash
  go install golang.org/x/tools/gopls@latest
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
3. Click "Add Plugins" and select `target/netbeans-go-*.nbm`
4. Install and restart NetBeans

### Development Mode
1. Open project in NetBeans IDE
2. Right-click project → Run
3. NetBeans creates temporary module for testing

## Configuration

### Go LSP Server Path
1. Tools → Options → Go
2. Set path to `gopls` executable
3. Default: Auto-detected from system PATH

### Go Interpreter
1. Tools → Options → Go
2. Set path to Go executable
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
- `GoLspServerLauncher`: Manages Go LSP server process (gopls)
- Extends `AbstractLspServerLauncher` from common module

### Code Completion
- `GoLspCompletionProvider`: Registers completion provider
- `GoLspCompletionQuery`: Queries LSP for completions
- `GoLspCompletionItem`: Individual completion items

### Project Management
- `GoProjectFactory`: Recognizes Go projects (go.mod presence)
- `GoProject`: Go project implementation

### Execution
- `RunGoAction`: Execute Go programs
- `GoConsoleTopComponent`: Interactive console (requires gore)

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
- Go installed
- Go LSP server installed
- NetBeans runtime environment

## Troubleshooting

### LSP Server Not Found
**Error**: "Go LSP server not found"
**Solution**: Install gopls:
```bash
go install golang.org/x/tools/gopls@latest
```

### No Code Completion
**Error**: Code completion not working
**Solutions**:
1. Verify gopls is installed
2. Check Go path in Tools → Options → Go
3. Check NetBeans version (22.0+ required)
4. View log: View → IDE Log for LSP errors

### ClassNotFoundException at Runtime
**Error**: `org.netbeans.modules.lsp.client.* not found`
**Solution**: Ensure NetBeans 22.0 or later is being used

### Go Console Not Working
**Error**: Console fails to start
**Solution**: Install gore REPL:
```bash
go install github.com/x-motemen/gore/cmd/gore@latest
```

## Development

### Adding LSP Features
1. Extend abstractions from `languages/common`
2. Implement Go-specific behavior
3. Register in `layer.xml`

### Module Structure
```
languages/go/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/flossware/netbeans/go/
│   │   │       ├── completion/     # Code completion
│   │   │       ├── debugger/       # Debugging support
│   │   │       ├── execution/      # Go execution
│   │   │       ├── lexer/         # Syntax highlighting
│   │   │       ├── lsp/           # LSP integration
│   │   │       ├── project/       # Project management
│   │   │       ├── settings/      # Configuration
│   │   │       └── ui/            # Console UI
│   │   └── resources/
│   │       └── org/flossware/netbeans/go/
│   │           ├── Bundle.properties
│   │           ├── GoResolver.xml
│   │           ├── FontsColors.xml
│   │           └── layer.xml
│   ├── test/                      # Unit tests
│   └── integration-test/          # Integration tests
└── README.md
```

## Go Language Features

### Supported File Types
- `.go` - Go source files

### Project Recognition
Projects are recognized by the presence of:
- `go.mod` - Go modules file
- `go.sum` - Go modules checksums
- `.go` files in directory (fallback)

### Syntax Highlighting
- Keywords: `package`, `import`, `func`, `var`, `const`, etc.
- Built-in types: `int`, `string`, `bool`, etc.
- Comments: Single-line (`//`) and multi-line (`/* */`)
- Strings: Regular (`"..."`) and raw (`` `...` ``)
- Numbers: Integers, floats, complex

### Code Completion
Powered by gopls, provides:
- Package imports
- Function/method completion
- Struct field completion
- Interface method completion
- Type-aware suggestions

## License

See LICENSE file in the root directory (GPL v3.0).

## Related Modules

- `languages/common`: Shared LSP abstractions
- `ai/claude`: Claude AI integration plugin
- `ai/gemini`: Gemini AI integration plugin
- `ai/chatgpt`: ChatGPT AI integration plugin

## Support

For issues or questions:
- GitHub Issues: https://github.com/FlossWare/netbeans-plugins/issues
- Check IDE Log: View → IDE Log
- Enable LSP logging for detailed diagnostics
