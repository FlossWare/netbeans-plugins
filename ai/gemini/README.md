# NetBeans Gemini Integration Plugin

AI-powered NetBeans IDE plugin integrating Google's Gemini AI with code completion, chat interface, and intelligent code actions.

## ✅ Status: Complete

All 23 classes implemented and fully functional.

## Features

- **AI-Powered Code Completion** - Intelligent suggestions via Ctrl+Space
- **Interactive Chat Window** - Dedicated Gemini chat interface with streaming responses
- **Editor Integration** - Right-click actions for code explanation and refactoring
- **Code Insertion** - Automatic detection and insertion of code blocks from responses
- **Project Context** - Includes project structure in queries for better suggestions
- **Configuration Panel** - Full control over API settings, models, and behavior

## Installation

### Prerequisites
- Apache NetBeans 24.0+ (RELEASE220)
- Java 11+
- Google AI API key from https://makersuite.google.com/app/apikey

### Build
```bash
mvn clean package
```

### Install
1. Tools → Plugins → Downloaded
2. Add Plugins... → Select `target/netbeans-gemini-integration-1.0.0-SNAPSHOT.nbm`
3. Install and restart NetBeans

### Configure
1. Tools → Options → Advanced Options → Gemini AI
2. Enter your Google AI API key
3. Select model (default: gemini-1.5-pro)
4. Configure completion settings
5. Test connection
6. Click OK

## Available Models

- **gemini-1.5-pro** (default) - Most capable, best for complex tasks
- **gemini-1.5-flash** - Fastest, optimized for speed
- **gemini-pro** - Standard model
- **gemini-pro-vision** - Multimodal with vision capabilities

## Usage

### Code Completion
1. Start typing in any Java file
2. Press **Ctrl+Space**
3. Select Gemini's AI-generated suggestion
4. Press **Enter** to insert

### Chat Interface
1. Tools → Open Gemini Chat
2. Type your question
3. View streaming response
4. Click **Insert Code** buttons to use suggestions

### Code Actions (Right-click menu)
- **Ask Gemini About This Code** - General questions about selected code
- **Explain This Code (Gemini)** - Get detailed explanation
- **Suggest Refactoring (Gemini)** - Get improvement suggestions

### Project Context
- Tools → Show Project Context (Gemini) - View what Gemini knows about your project

## Configuration Options

### API Settings
- **API Key** - Your Google AI API key (stored securely)
- **Model** - Which Gemini model to use
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
org.flossware.netbeans.gemini/
├── api/          - Google Gemini API client integration (REST)
├── completion/   - Code completion implementation (NetBeans SPI)
├── ui/           - Chat window and dialogs
├── actions/      - Menu and toolbar actions
├── options/      - Settings panel
├── context/      - Project context extraction
└── util/         - Helper utilities
```

### Key Technologies
- **Google Cloud AI Platform** - Gemini API via REST
- **OkHttp** - HTTP client for API calls
- **NetBeans Platform APIs** - IDE integration
- **Async Processing** - RequestProcessor for non-blocking operations
- **Smart Caching** - TTL-based cache for performance

## Building from Source

```bash
git clone <repository-url>
cd netbeans-gemini
mvn clean package
```

## Companion Plugins

This plugin is part of a family of three NetBeans AI plugins:
- **netbeans-claude** - Anthropic Claude integration
- **netbeans-gemini** - This plugin (Google Gemini)
- **netbeans-chatgpt** - OpenAI ChatGPT integration

All three can be installed simultaneously without conflicts.

## Differences from Claude Plugin

### API Implementation
- Uses Google Generative AI REST API instead of Anthropic SDK
- Different model naming (gemini-1.5-pro vs claude-sonnet-4-5)
- REST-based streaming vs. SDK streaming

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
