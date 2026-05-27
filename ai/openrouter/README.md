# NetBeans OpenRouter Integration

OpenRouter AI integration for Apache NetBeans IDE.

## Overview

This module integrates OpenRouter AI into NetBeans, providing access to multiple AI models including:
- Anthropic Claude (Claude 3.5 Sonnet, Claude 3 Opus, Claude 3 Haiku)
- OpenAI GPT-4 and GPT-3.5
- Google Gemini Pro
- Meta Llama 3
- Mistral AI models
- And many more

## Features

- Chat interface for AI assistance
- Code explanation and analysis
- Context-aware queries with project information
- Right-click "Ask OpenRouter" action on selected code
- Configurable model selection
- Streaming responses for real-time interaction

## Configuration

1. Get an API key from [OpenRouter](https://openrouter.ai/keys)
2. In NetBeans, go to Tools > Options > Advanced > OpenRouter AI
3. Enter your API key
4. Select your preferred model
5. (Optional) Adjust max tokens and temperature settings

## Usage

### Chat Window

Open the OpenRouter chat window from:
- Menu: Window > OpenRouter
- Tools menu: Tools > Open OpenRouter Chat

### Code Actions

Right-click on selected code in the editor and choose "Ask OpenRouter" to get AI assistance about the selected code.

## API Endpoint

This plugin uses the OpenRouter API endpoint:
- URL: https://openrouter.ai/api/v1/chat/completions
- Authentication: Bearer token in Authorization header

## Default Model

The default model is `anthropic/claude-3.5-sonnet`, but you can change this in the settings to any model supported by OpenRouter.

## License

GPL v3.0 - See LICENSE file for details.

## Copyright

Copyright 2026 FlossWare
