# NetBeans ChatGPT Integration Plugin

AI-powered NetBeans IDE plugin integrating OpenAI's ChatGPT with code completion, chat interface, and intelligent code actions.

## ✅ Status: Complete

All 23 classes implemented and fully functional.

## Features

- **AI-Powered Code Completion** - Intelligent suggestions via Ctrl+Space
- **Interactive Chat Window** - Dedicated ChatGPT chat interface with streaming responses
- **Editor Integration** - Right-click actions for code explanation and refactoring
- **Code Insertion** - Automatic detection and insertion of code blocks from responses
- **Project Context** - Includes project structure in queries for better suggestions
- **Configuration Panel** - Full control over API settings, models, and behavior

## Installation

### Prerequisites
- Apache NetBeans 24.0+ (RELEASE220)
- Java 11+
- OpenAI API key from https://console.openai.com/

### Build
```bash
mvn clean package
```

### Install
1. Tools → Plugins → Downloaded
2. Add Plugins... → Select `target/netbeans-chatgpt-integration-1.0.0-SNAPSHOT.nbm`
3. Install and restart NetBeans

### Configure
1. Tools → Options → Advanced Options → ChatGPT AI
2. Enter your OpenAI API key
3. Select model (default: gpt-4-turbo-preview)
4. Configure completion settings
5. Test connection
6. Click OK

## Available Models

- **gpt-4-turbo-preview** (default) - Latest GPT-4 with improved capabilities
- **gpt-4** - Most capable GPT-4 model
- **gpt-3.5-turbo** - Fast and efficient
- **gpt-3.5-turbo-16k** - Extended context window

## Usage

### Code Completion
1. Start typing in any Java file
2. Press **Ctrl+Space**
3. Select ChatGPT's AI-generated suggestion
4. Press **Enter** to insert

### Chat Interface
1. Tools → Open ChatGPT Chat
2. Type your question
3. View streaming response
4. Click **Insert Code** buttons to use suggestions

### Code Actions (Right-click menu)
- **Ask ChatGPT About This Code** - General questions about selected code
- **Explain This Code (ChatGPT)** - Get detailed explanation
- **Suggest Refactoring (ChatGPT)** - Get improvement suggestions

### Project Context
- Tools → Show Project Context (ChatGPT) - View what ChatGPT knows about your project

## Configuration Options

### API Settings
- **API Key** - Your OpenAI API key (stored securely)
- **Model** - Which GPT model to use
- **Max Tokens** - Maximum response length (1024-200000)
- **Temperature** - Randomness in responses (0.0-1.0)

### Completion Settings
- **Enable Completion** - Toggle AI code completion
- **Auto-trigger** - Trigger on typing vs. Ctrl+Space only
- **Minimum Characters** - How many characters before triggering (1-10)
- **Trigger Characters** - Specific characters that trigger completion (default: ".")
- **Enable Cache** - Reduce API calls by caching suggestions (5-minute TTL)

### Project Context
- **Enable Project Context** - Include project file structure in queries

## Architecture

```
org.flossware.netbeans.chatgpt/
├── api/          - OpenAI API client integration
├── completion/   - Code completion implementation (NetBeans SPI)
├── ui/           - Chat window and dialogs
├── actions/      - Menu and toolbar actions
├── options/      - Settings panel
├── context/      - Project context extraction
└── util/         - Helper utilities
```

### Key Technologies
- **OpenAI GPT3 Java SDK** - Official Java client (theokanning)
- **RxJava2** - Reactive streaming support
- **NetBeans Platform APIs** - IDE integration
- **Async Processing** - RequestProcessor for non-blocking operations
- **Smart Caching** - TTL-based cache for performance

## Building from Source

```bash
git clone <repository-url>
cd netbeans-chatgpt
mvn clean package
```

## Companion Plugins

This plugin is part of a family of three NetBeans AI plugins:
- **netbeans-claude** - Anthropic Claude integration
- **netbeans-gemini** - Google Gemini integration
- **netbeans-chatgpt** - This plugin (OpenAI ChatGPT)

All three can be installed simultaneously without conflicts.

## Differences from Claude Plugin

### API Implementation
- Uses OpenAI SDK (theokanning/openai-gpt3-java) instead of Anthropic SDK
- Different model naming (gpt-4-turbo-preview vs claude-sonnet-4-5)
- SDK-based streaming via RxJava2

### Features
- Same feature set as Claude plugin
- Same UI and workflow
- Only the AI backend differs

## Troubleshooting

### Build Issues
If build fails with missing NetBeans dependencies, see `../BUILD_STATUS.md` for solutions to corporate Maven mirror issues.

### Code Completion Not Appearing
1. Verify API key is configured
2. Check completion is enabled in settings
3. Ensure minimum character threshold is met
4. Try manual trigger (Ctrl+Space)
5. Check View → IDE Log for errors

### Connection Errors
1. Verify API key is correct
2. Check internet connectivity
3. Use Test Connection button
4. Review IDE log for details

## License

Apache License 2.0

## Credits

**Author**: FlossWare  
**AI Assistant**: Claude Sonnet 4.5  
**Generated From**: netbeans-claude plugin architecture
