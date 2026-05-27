# Mistral AI Integration for NetBeans

Integrates Mistral AI into Apache NetBeans IDE for AI-powered code assistance.

## Features

- 💬 Interactive AI chat window
- 🔍 Code explanation and analysis
- ✨ Smart code completion suggestions
- 📝 Context-aware responses using project information
- ⚙️ Configurable model selection

## Free Tier ✅

**Mistral offers a FREE tier!**
- **Free models**: `mistral-small-latest`, `open-mistral-7b`
- **Default model**: `mistral-small-latest` (FREE)
- **Get API key**: https://console.mistral.ai
- No credit card required for free tier
- Generous free quota for development

## Setup

1. **Get API Key** (FREE):
   - Visit https://console.mistral.ai
   - Create account (no credit card needed)
   - Generate API key

2. **Configure in NetBeans**:
   - Tools → Options → Advanced → Mistral AI
   - Enter your API key
   - Click "Test Connection"
   - Select model (default: mistral-small-latest - FREE)

3. **Usage**:
   - Right-click code → "Ask Mistral AI"
   - Window → Mistral AI (open chat)
   - Select code + right-click for context-aware queries

## Available Models

### Free Models ✅
- `mistral-small-latest` - **FREE** (default) - Fast, efficient for most tasks
- `open-mistral-7b` - **FREE** - Open source, completely free

### Paid Models 💰
- `mistral-medium-latest` - Better performance
- `mistral-large-latest` - Most capable
- `codestral-latest` - Specialized for code

## Requirements

- NetBeans 22.0+
- Java 11+
- Mistral API key (free tier available)

## Privacy

- All requests sent to Mistral AI servers
- Code context sent to API (be mindful of sensitive data)
- Review Mistral's privacy policy: https://mistral.ai/terms/

## Support

- Mistral Docs: https://docs.mistral.ai
- API Reference: https://docs.mistral.ai/api
- Get Help: https://discord.gg/mistralai
