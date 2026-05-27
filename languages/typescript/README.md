# NetBeans TypeScript Language Support Plugin

LSP-based TypeScript language support for NetBeans IDE.

## Features

- **Code Completion**: Intelligent code completion via TypeScript LSP
- **Project Management**: TypeScript project recognition and management
- **Code Execution**: Run TypeScript scripts from within NetBeans using ts-node
- **Console Integration**: Interactive TypeScript console (REPL)
- **LSP Integration**: Full Language Server Protocol support
- **TypeScript & TSX Support**: Supports both .ts and .tsx files

## Requirements

### Minimum NetBeans Version
- **NetBeans 22.0 or later** (RELEASE220+)
- Required for LSP client support (`org.netbeans.modules.lsp.client`)

### External Dependencies
- **Node.js**: Node.js 14.0 or later installed on your system
- **TypeScript**: TypeScript compiler installed
  ```bash
  npm install -g typescript
  ```
- **TypeScript Language Server**: `typescript-language-server` installed
  ```bash
  npm install -g typescript-language-server
  ```
- **ts-node** (optional, for running scripts and REPL):
  ```bash
  npm install -g ts-node
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
3. Click "Add Plugins" and select `target/netbeans-typescript-*.nbm`
4. Install and restart NetBeans

### Development Mode
1. Open project in NetBeans IDE
2. Right-click project → Run
3. NetBeans creates temporary module for testing

## Configuration

### TypeScript Language Server Path
1. Tools → Options → TypeScript
2. Set path to `typescript-language-server` executable
3. Default: Auto-detected from system PATH

### Node.js Interpreter
1. Tools → Options → TypeScript
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
- `TypeScriptLspServerLauncher`: Manages TypeScript LSP server process
- Extends `AbstractLspServerLauncher` from common module

### Code Completion
- `TypeScriptLspCompletionProvider`: Registers completion provider
- `TypeScriptLspCompletionQuery`: Queries LSP for completions
- `TypeScriptLspCompletionItem`: Individual completion items
- **Auto-triggers**: Completion automatically triggers on `.` and `:`

### Project Management
- `TypeScriptProjectFactory`: Recognizes TypeScript projects
- `TypeScriptProject`: TypeScript project implementation
- **Project Markers**: Recognizes projects by `tsconfig.json` or `package.json`

### Execution
- `RunTypeScriptAction`: Execute TypeScript scripts using ts-node
- `TypeScriptConsoleTopComponent`: Interactive TypeScript console (REPL)

### Lexer (Syntax Highlighting)
- `TypeScriptTokenId`: Token types for syntax highlighting
- `TypeScriptLexer`: Lexical analyzer for TypeScript
- `TypeScriptLanguageHierarchy`: Language hierarchy registration

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
- TypeScript and TypeScript Language Server installed
- ts-node installed
- NetBeans runtime environment

## Troubleshooting

### LSP Server Not Found
**Error**: "TypeScript LSP server not found"
**Solution**: Install typescript-language-server:
```bash
npm install -g typescript-language-server typescript
```

### No Code Completion
**Error**: Code completion not working
**Solutions**:
1. Verify TypeScript Language Server is installed
2. Check Node.js path in Tools → Options → TypeScript
3. Check NetBeans version (22.0+ required)
4. View log: View → IDE Log for LSP errors

### Script Execution Fails
**Error**: "ts-node: command not found"
**Solution**: Install ts-node:
```bash
npm install -g ts-node
```

### ClassNotFoundException at Runtime
**Error**: `org.netbeans.modules.lsp.client.* not found`
**Solution**: Ensure NetBeans 22.0 or later is being used

## Development

### Adding LSP Features
1. Extend abstractions from `languages/common`
2. Implement TypeScript-specific behavior
3. Register in `layer.xml`

### Module Structure
```
languages/typescript/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/flossware/netbeans/typescript/
│   │   │       ├── completion/     # Code completion
│   │   │       ├── execution/      # TypeScript execution
│   │   │       ├── debugger/       # Debugger integration
│   │   │       ├── lsp/           # LSP integration
│   │   │       ├── lexer/         # Syntax highlighting
│   │   │       ├── project/       # Project management
│   │   │       ├── settings/      # Configuration
│   │   │       └── ui/            # Console UI
│   │   └── resources/
│   │       └── org/flossware/netbeans/typescript/
│   │           ├── Bundle.properties
│   │           ├── TypeScriptResolver.xml
│   │           ├── FontsColors.xml
│   │           └── layer.xml
│   ├── test/                      # Unit tests
│   └── integration-test/          # Integration tests
└── README.md
```

## TypeScript-Specific Features

### Supported File Extensions
- `.ts` - TypeScript files
- `.tsx` - TypeScript JSX files (React components)

### TypeScript Keywords
The lexer recognizes all TypeScript keywords including:
- Type keywords: `type`, `interface`, `enum`, `namespace`
- Access modifiers: `public`, `private`, `protected`, `readonly`
- Type operators: `keyof`, `typeof`, `is`, `as`
- Advanced types: `any`, `unknown`, `never`, `unique`

### Project Recognition
A directory is considered a TypeScript project if it contains:
- `tsconfig.json` (TypeScript configuration)
- `package.json` (npm package with TypeScript dependencies)

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
