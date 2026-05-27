# NetBeans C# Language Support Plugin

LSP-based C# language support for NetBeans IDE.

## Features

- **Code Completion**: Intelligent code completion via C# LSP (OmniSharp)
- **Project Management**: C# project recognition and management
- **Code Execution**: Run C# scripts from within NetBeans
- **Console Integration**: Interactive C# console (REPL)
- **LSP Integration**: Full Language Server Protocol support

## Requirements

### Minimum NetBeans Version
- **NetBeans 22.0 or later** (RELEASE220+)
- Required for LSP client support (`org.netbeans.modules.lsp.client`)

### External Dependencies
- **.NET SDK**: .NET 6.0 or later installed on your system
- **OmniSharp**: OmniSharp C# language server installed
  ```bash
  dotnet tool install -g omnisharp
  ```
- **dotnet-script** (for REPL): Optional, for interactive console
  ```bash
  dotnet tool install -g dotnet-script
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
3. Click "Add Plugins" and select `target/netbeans-csharp-*.nbm`
4. Install and restart NetBeans

### Development Mode
1. Open project in NetBeans IDE
2. Right-click project → Run
3. NetBeans creates temporary module for testing

## Configuration

### OmniSharp Language Server Path
1. Tools → Options → C#
2. Set path to `omnisharp` executable
3. Default: Auto-detected from system PATH

### .NET SDK Path
1. Tools → Options → C#
2. Set path to dotnet executable
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
- `CSharpLspServerLauncher`: Manages OmniSharp LSP server process
- Extends `AbstractLspServerLauncher` from common module

### Code Completion
- `CSharpLspCompletionProvider`: Registers completion provider
- `CSharpLspCompletionQuery`: Queries LSP for completions
- `CSharpLspCompletionItem`: Individual completion items

### Project Management
- `CSharpProjectFactory`: Recognizes C# projects
- `CSharpProject`: C# project implementation

### Execution
- `RunCSharpAction`: Execute C# scripts
- `CSharpConsoleTopComponent`: Interactive console

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
- .NET SDK installed
- OmniSharp installed
- NetBeans runtime environment

## Troubleshooting

### OmniSharp Not Found
**Error**: "C# LSP server not found"
**Solution**: Install OmniSharp:
```bash
dotnet tool install -g omnisharp
```

### No Code Completion
**Error**: Code completion not working
**Solutions**:
1. Verify OmniSharp is installed
2. Check .NET SDK path in Tools → Options → C#
3. Check NetBeans version (22.0+ required)
4. View log: View → IDE Log for LSP errors

### ClassNotFoundException at Runtime
**Error**: `org.netbeans.modules.lsp.client.* not found`
**Solution**: Ensure NetBeans 22.0 or later is being used

### Console Not Working
**Error**: C# console (REPL) not starting
**Solution**: Install dotnet-script:
```bash
dotnet tool install -g dotnet-script
```

## Development

### Adding LSP Features
1. Extend abstractions from `languages/common`
2. Implement C#-specific behavior
3. Register in `layer.xml`

### Module Structure
```
languages/csharp/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/flossware/netbeans/csharp/
│   │   │       ├── completion/     # Code completion
│   │   │       ├── execution/      # C# execution
│   │   │       ├── lsp/           # LSP integration
│   │   │       ├── project/       # Project management
│   │   │       ├── settings/      # Configuration
│   │   │       └── ui/            # Console UI
│   │   └── resources/
│   │       └── org/flossware/netbeans/csharp/
│   │           ├── Bundle.properties
│   │           ├── CSharpResolver.xml
│   │           └── layer.xml
│   ├── test/                      # Unit tests
│   └── integration-test/          # Integration tests
└── README.md
```

## C# Project Recognition

A directory is recognized as a C# project if it contains any of:
- `*.csproj` - C# project file
- `*.sln` - Visual Studio solution file
- `global.json` - .NET SDK version specification
- `.cs` files (fallback)

## Supported C# Features

### Syntax Highlighting
- Keywords (class, namespace, using, etc.)
- String literals and character literals
- Comments (single-line and multi-line)
- Numbers and operators

### Code Completion
- Type completion
- Member completion (methods, properties)
- Namespace completion
- Using directive completion

### Execution
- Run C# files with `dotnet script`
- Interactive console with `dotnet-script`

## License

See LICENSE file in the ai/claude directory (shared license).

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
