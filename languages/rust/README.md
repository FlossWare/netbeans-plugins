# NetBeans Rust Language Support Plugin

LSP-based Rust language support for NetBeans IDE.

## Features

- **Code Completion**: Intelligent code completion via rust-analyzer LSP
- **Project Management**: Rust/Cargo project recognition and management
- **Code Execution**: Run Rust programs from within NetBeans
- **Console Integration**: Interactive Rust console (via evcxr)
- **LSP Integration**: Full Language Server Protocol support

## Requirements

### Minimum NetBeans Version
- **NetBeans 22.0 or later** (RELEASE220+)
- Required for LSP client support (`org.netbeans.modules.lsp.client`)

### External Dependencies
- **Rust**: Rust 1.70 or later installed on your system
- **rust-analyzer**: Language server for Rust
  ```bash
  rustup component add rust-analyzer
  ```
- **evcxr** (optional): For interactive Rust REPL
  ```bash
  cargo install evcxr_repl
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
3. Click "Add Plugins" and select `target/netbeans-rust-*.nbm`
4. Install and restart NetBeans

### Development Mode
1. Open project in NetBeans IDE
2. Right-click project → Run
3. NetBeans creates temporary module for testing

## Configuration

### Rust Analyzer Path
1. Tools → Options → Rust
2. Set path to `rust-analyzer` executable
3. Default: Auto-detected from system PATH

### Rust Compiler
1. Tools → Options → Rust
2. Set path to Rust compiler
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
- `RustLspServerLauncher`: Manages rust-analyzer LSP server process
- Extends `AbstractLspServerLauncher` from common module

### Code Completion
- `RustLspCompletionProvider`: Registers completion provider
- `RustLspCompletionQuery`: Queries LSP for completions
- `RustLspCompletionItem`: Individual completion items

### Project Management
- `RustProjectFactory`: Recognizes Rust/Cargo projects
- `RustProject`: Rust project implementation

### Execution
- `RunRustAction`: Execute Rust programs via cargo
- `RustConsoleTopComponent`: Interactive console (evcxr)

### Lexer
- `RustTokenId`: Token types for syntax highlighting
- `RustLanguageHierarchy`: Language hierarchy for Rust
- `RustLexer`: Basic lexer for Rust syntax

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
- Rust installed
- rust-analyzer installed
- NetBeans runtime environment

## Troubleshooting

### LSP Server Not Found
**Error**: "Rust LSP server not found"
**Solution**: Install rust-analyzer:
```bash
rustup component add rust-analyzer
```

### No Code Completion
**Error**: Code completion not working
**Solutions**:
1. Verify rust-analyzer is installed
2. Check Rust path in Tools → Options → Rust
3. Check NetBeans version (22.0+ required)
4. View log: View → IDE Log for LSP errors

### ClassNotFoundException at Runtime
**Error**: `org.netbeans.modules.lsp.client.* not found`
**Solution**: Ensure NetBeans 22.0 or later is being used

## Development

### Adding LSP Features
1. Extend abstractions from `languages/common`
2. Implement Rust-specific behavior
3. Register in `layer.xml`

### Module Structure
```
languages/rust/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/flossware/netbeans/rust/
│   │   │       ├── completion/     # Code completion
│   │   │       ├── debugger/       # Debugger support
│   │   │       ├── execution/      # Rust execution
│   │   │       ├── lexer/          # Syntax highlighting
│   │   │       ├── lsp/            # LSP integration
│   │   │       ├── project/        # Project management
│   │   │       ├── settings/       # Configuration
│   │   │       └── ui/             # Console UI
│   │   └── resources/
│   │       └── org/flossware/netbeans/rust/
│   │           ├── Bundle.properties
│   │           ├── RustResolver.xml
│   │           ├── FontsColors.xml
│   │           └── layer.xml
│   ├── test/                       # Unit tests
│   └── integration-test/           # Integration tests
└── README.md
```

## License

See LICENSE file in the root directory (GPL v3.0).

## Related Modules

- `languages/common`: Shared LSP abstractions
- `languages/python`: Python language support
- `languages/go`: Go language support
- `ai/claude`: Claude AI integration plugin

## Support

For issues or questions:
- GitHub Issues: https://github.com/FlossWare/netbeans-plugins/issues
- Check IDE Log: View → IDE Log
- Enable LSP logging for detailed diagnostics

## Rust-Specific Notes

### Project Recognition
Rust projects are recognized by:
- `Cargo.toml` (Cargo package manifest)
- `Cargo.lock` (Cargo lock file)
- `.rs` files in project root (fallback)

### Code Completion
The plugin uses rust-analyzer for intelligent code completion, which provides:
- Type inference
- Trait completion
- Macro expansion
- Import suggestions
- Method completion

### REPL Support
The Rust console uses `evcxr_repl`, an evaluation context for Rust. Install with:
```bash
cargo install evcxr_repl
```

Then use from Window → Rust Console in NetBeans.
