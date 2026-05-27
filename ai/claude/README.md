# NetBeans Claude Integration

A comprehensive NetBeans IDE plugin that integrates Anthropic's Claude AI assistant directly into your development environment with advanced features for code assistance, project awareness, and seamless editor integration.

## Features

### ✅ Core Features (Implemented)

- **⚡ AI-Powered Code Completion**: Real-time code suggestions as you type
  - Ctrl+Space integration with NetBeans autocomplete
  - Optional auto-trigger on specific characters
  - Smart caching to reduce API calls
  - Context-aware completions based on surrounding code
- **🗨️ Chat Interface**: Interactive chat window for conversing with Claude
- **⚙️ Options Panel**: Full settings UI for API key and model configuration
- **📝 Editor Integration**: Right-click context menu integration
  - Ask Claude about selected code
  - Explain code functionality
  - Get refactoring suggestions
- **🌊 Streaming Responses**: Real-time response streaming for better UX
- **📋 Code Insertion**: Extract and insert code from Claude responses directly into editor
- **🗂️ Project Context Awareness**: Automatic project structure understanding
  - Reads project files and structure
  - Provides relevant context to Claude
  - Understands related files (tests, interfaces, etc.)
- **💬 Conversation History**: Maintains context across multiple messages
- **🎯 Multiple Model Support**: Choose from Sonnet, Opus, or Haiku
- **🔧 Configurable Settings**: Temperature, max tokens, completion settings, and more
- **🧪 Test Generation**: AI-powered test generation from selected code
  - Generates JUnit 5 tests with Mockito and AssertJ
  - Comprehensive coverage with edge cases
  - Automatic test file creation
- **📚 Javadoc Generation**: Auto-generate documentation from methods
  - Complete javadoc with @param, @return, @throws
  - Inserts directly into editor
  - Context-aware descriptions

## Prerequisites

- Apache NetBeans 22.0 or later
- Java 11 or later
- Anthropic API key ([Get one here](https://console.anthropic.com/))

## Building the Plugin

```bash
mvn clean package
```

This will create an NBM (NetBeans Module) file in the `target` directory.

## Installation

### Option 1: Build and Install from Source

1. Clone this repository
2. Build the project: `mvn clean package`
3. In NetBeans, go to **Tools > Plugins**
4. Click the **Downloaded** tab
5. Click **Add Plugins** and select `target/netbeans-claude-integration-1.0.0-SNAPSHOT.nbm`
6. Click **Install** and follow the wizard
7. Restart NetBeans

### Option 2: Install from NBM File

1. Download the NBM file from releases
2. In NetBeans, go to **Tools > Plugins > Downloaded**
3. Click **Add Plugins** and select the NBM file
4. Click **Install**
5. Restart NetBeans

## Configuration

1. After installation and restart, go to **Tools > Options**
2. Navigate to **Advanced Options > Claude AI**
3. Enter your Anthropic API key
4. Configure optional settings:
   - **Model**: Choose Claude model (Sonnet, Opus, Haiku)
   - **Max Tokens**: Set response length limit (1024-200000)
   - **Temperature**: Control response creativity (0.0-1.0)
   - **Enable Project Context**: Toggle project awareness
5. Click **Test Connection** to verify setup
6. Click **OK** to save

## Usage

### Open Claude Chat Window

- From menu: **Tools > Open Claude Chat**
- Or find "Claude" in the **Window** menu
- Keyboard shortcut (if configured)

### Basic Chat

1. Type your question or request in the input field at the bottom
2. Press Enter or click **Send**
3. Claude will stream the response in real-time
4. Responses appear in the chat area above

### Editor Context Menu Actions

Select code in the editor, right-click, and choose:

1. **Ask Claude About This Code** - General questions about selected code
2. **Explain This Code (Claude)** - Detailed explanation of functionality
3. **Suggest Refactoring (Claude)** - Get improvement suggestions
4. **Generate Test (Claude)** - Auto-generate comprehensive JUnit 5 tests
5. **Generate Javadoc (Claude)** - Create documentation for methods

### Code Insertion

When Claude provides code in responses:
1. Look for **Insert Code** buttons below the response
2. Click the button for the code block you want
3. Choose to insert at cursor or replace selection
4. Edit if needed and confirm

### Project Context

View your project context:
- **Tools > Show Project Context (Claude)**
- Shows project structure, important files, and summary
- Automatically included with queries when enabled

### Clear Conversation

Click the **Clear** button to reset the conversation history and start fresh.

## Architecture

### Module Structure

```
org.flossware.netbeans.claude
├── api/                         # Claude API integration
│   ├── ClaudeClient.java        # Direct API client with streaming
│   └── ClaudeService.java       # Async service layer
├── ui/                          # User interface
│   ├── ClaudeWindowTopComponent.java  # Main chat window
│   ├── CodeInsertDialog.java   # Code insertion dialog
│   └── ChatMessagePanel.java   # Message display with code buttons
├── actions/                     # NetBeans actions
│   ├── OpenClaudeAction.java
│   ├── AskClaudeAboutSelectionAction.java
│   ├── ExplainCodeAction.java
│   ├── RefactorWithClaudeAction.java
│   └── ShowProjectContextAction.java
├── options/                     # Settings panel
│   ├── ClaudeOptionsPanelController.java
│   └── ClaudeOptionsPanel.java
├── context/                     # Project awareness
│   ├── ProjectContext.java
│   └── ProjectContextManager.java
└── util/                        # Utilities
    ├── CodeExtractor.java       # Extract code blocks from text
    └── EditorUtil.java          # Editor interaction utilities
```

### Key Components

- **ClaudeClient**: Handles direct communication with Anthropic API, supports streaming
- **ClaudeService**: Provides async/threaded operations for UI responsiveness
- **ClaudeWindowTopComponent**: The main chat UI window with streaming support
- **ProjectContextManager**: Manages project awareness and context building
- **CodeExtractor**: Parses Claude responses for code blocks
- **EditorUtil**: Utilities for inserting code into NetBeans editor

## API Models Supported

- **claude-sonnet-4-5@20250929** (Default) - Balanced performance
- **claude-opus-4-7** - Most capable, slower
- **claude-sonnet-4-6** - Previous Sonnet version
- **claude-haiku-4-5-20251001** - Fastest, lower cost

## Configuration Options

| Setting | Description | Default |
|---------|-------------|---------|
| API Key | Anthropic API key | (required) |
| Model | Claude model to use | claude-sonnet-4-5 |
| Max Tokens | Maximum response length | 4096 |
| Temperature | Response creativity (0-1) | 1.0 |
| Enable Project Context | Include project info in queries | true |

## Features in Detail

### Streaming Responses

Responses now stream in real-time as Claude generates them, providing immediate feedback and better user experience.

### Project Context Awareness

When enabled, the plugin:
- Analyzes your project structure
- Identifies important files (pom.xml, package.json, etc.)
- Reads relevant source files
- Finds related files (tests, implementations)
- Includes context automatically with your queries

### Code Insertion

Claude's code responses are automatically detected and made available for insertion:
- Multiple code blocks per response supported
- Language detection (Java, Python, JavaScript, etc.)
- Insert at cursor or replace selection
- Edit before inserting
- Preserves formatting

## Troubleshooting

### Build Fails

- Check Java version: `java -version` (needs 11+)
- Check Maven version: `mvn -version` (needs 3.6+)
- Clear Maven cache: `mvn clean`

### Plugin Won't Install

- Verify NBM file exists in `target/`
- Check NetBeans version (needs 22.0+)
- Try restarting NetBeans

### API Calls Failing

- Verify API key is correct in Tools > Options
- Check internet connection
- Verify you have API credits (https://console.anthropic.com/)
- Check NetBeans IDE log: **View > IDE Log**

### Streaming Not Working

- Check network connection
- Verify API key has streaming permissions
- Try switching to a different model

### Project Context Not Loading

- Ensure "Enable project context" is checked in Options
- Verify you have a project open
- Check that project files are readable
- Some file types may be excluded for size/relevance

## Development

### Running in Development Mode

1. Open this project in NetBeans
2. Right-click project > **Run**
3. A new NetBeans instance launches with your plugin
4. Make changes, stop, and run again

### Debug Mode

1. Set breakpoints in your code
2. Right-click project > **Debug**
3. Debug in the launched NetBeans instance

## Contributing

Contributions welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## Roadmap

Future enhancements to consider:
- [ ] Multiple conversation tabs
- [ ] Export conversation history
- [ ] Syntax highlighting in responses
- [ ] Custom prompts and templates
- [ ] Keyboard shortcuts customization
- [ ] Tool use support (file operations, running commands)
- [ ] Integration with NetBeans tasks/issues
- [ ] Code review mode
- [ ] Diff preview before code insertion
- [ ] Search conversation history

## License

Apache License 2.0 - see [LICENSE](LICENSE)

## Credits

- Created by FlossWare
- Powered by Anthropic's Claude API
- Built on Apache NetBeans Platform

## Support

For issues and questions:
- Open an issue on GitHub
- Check NetBeans plugin documentation: https://netbeans.apache.org/tutorials/

## Acknowledgments

- Anthropic for the Claude API
- Apache NetBeans community
- Inspired by similar IDE integrations

## Changelog

### Version 1.0.0-SNAPSHOT

- ✅ Initial release
- ✅ Chat interface with streaming
- ✅ Options panel
- ✅ Editor integration (3 context menu actions)
- ✅ Code insertion capabilities
- ✅ Project context awareness
- ✅ Multiple model support
- ✅ Configurable settings
