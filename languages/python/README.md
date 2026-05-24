# NetBeans Python Language Support Plugin

LSP-based Python language support for NetBeans IDE.

## Features

- **Code Completion**: Intelligent code completion via Python LSP
- **Project Management**: Python project recognition and management
- **Code Execution**: Run Python scripts from within NetBeans
- **Console Integration**: Interactive Python console
- **LSP Integration**: Full Language Server Protocol support

## Requirements

### Minimum NetBeans Version
- **NetBeans 22.0 or later** (RELEASE220+)
- Required for LSP client support (`org.netbeans.modules.lsp.client`)

### External Dependencies
- **Python**: Python 3.8 or later installed on your system
- **Python LSP Server**: `python-lsp-server` (pylsp) installed
  ```bash
  pip install python-lsp-server
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
3. Click "Add Plugins" and select `target/netbeans-python-*.nbm`
4. Install and restart NetBeans

### Development Mode
1. Open project in NetBeans IDE
2. Right-click project → Run
3. NetBeans creates temporary module for testing

## Configuration

### Python LSP Server Path
1. Tools → Options → Python
2. Set path to `pylsp` executable
3. Default: Auto-detected from system PATH

### Python Interpreter
1. Tools → Options → Python
2. Set path to Python interpreter
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
- `PythonLspServerLauncher`: Manages Python LSP server process
- Extends `AbstractLspServerLauncher` from common module

### Code Completion
- `PythonLspCompletionProvider`: Registers completion provider
- `PythonLspCompletionQuery`: Queries LSP for completions
- `PythonLspCompletionItem`: Individual completion items

### Project Management
- `PythonProjectFactory`: Recognizes Python projects
- `PythonProject`: Python project implementation

### Execution
- `RunPythonAction`: Execute Python scripts
- `PythonConsoleTopComponent`: Interactive console

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
- Python installed
- Python LSP server installed
- NetBeans runtime environment

## Troubleshooting

### LSP Server Not Found
**Error**: "Python LSP server not found"
**Solution**: Install python-lsp-server:
```bash
pip install python-lsp-server
```

### No Code Completion
**Error**: Code completion not working
**Solutions**:
1. Verify Python LSP server is installed
2. Check Python path in Tools → Options → Python
3. Check NetBeans version (22.0+ required)
4. View log: View → IDE Log for LSP errors

### ClassNotFoundException at Runtime
**Error**: `org.netbeans.modules.lsp.client.* not found`
**Solution**: Ensure NetBeans 22.0 or later is being used

## Development

### Adding LSP Features
1. Extend abstractions from `languages/common`
2. Implement Python-specific behavior
3. Register in `layer.xml`

### Module Structure
```
languages/python/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/flossware/netbeans/python/
│   │   │       ├── completion/     # Code completion
│   │   │       ├── execution/      # Python execution
│   │   │       ├── lsp/           # LSP integration
│   │   │       ├── project/       # Project management
│   │   │       ├── settings/      # Configuration
│   │   │       └── ui/            # Console UI
│   │   └── resources/
│   │       └── org/flossware/netbeans/python/
│   │           ├── Bundle.properties
│   │           ├── PythonResolver.xml
│   │           └── layer.xml
│   ├── test/                      # Unit tests
│   └── integration-test/          # Integration tests
└── README.md
```

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
