# Perplexity AI Integration for NetBeans

Integrates Perplexity AI into Apache NetBeans IDE - search-focused AI with web browsing capabilities.

## Features

- 💬 Interactive AI chat window with web search
- 🌐 Real-time web browsing for current information
- 🔍 Code explanation with latest documentation
- 📝 Context-aware responses using project + web data
- ⚙️ Configurable model selection

## Free Tier ⚠️

**Perplexity has LIMITED free tier**
- **Free tier**: 5 API requests per day
- **Default model**: `llama-3.1-sonar-small-128k-online` (optimized for free tier)
- **Get API key**: https://www.perplexity.ai/settings/api
- **Paid tier**: $20/month for unlimited requests

⚠️ **Note**: The free tier is quite limited. Consider using for important queries only, or upgrade to paid tier for regular use.

## Setup

1. **Get API Key**:
   - Visit https://www.perplexity.ai/settings/api
   - Sign in to your Perplexity account
   - Generate API key (5 requests/day free)

2. **Configure in NetBeans**:
   - Tools → Options → Advanced → Perplexity AI
   - Enter your API key
   - Click "Test Connection"
   - Select model (default uses smaller model to conserve quota)

3. **Usage**:
   - Right-click code → "Ask Perplexity AI"
   - Window → Perplexity AI (open chat)
   - Best for queries needing current/web information

## Available Models

### Online Models (with web search) 🌐
- `llama-3.1-sonar-small-128k-online` - **Default** - Smaller, conserves quota
- `llama-3.1-sonar-large-128k-online` - Larger, more capable
- `llama-3.1-sonar-huge-128k-online` - Most capable with web search

### Chat Models (no web search) 💬  
- `llama-3.1-sonar-small-128k-chat` - Smaller chat-only
- `llama-3.1-sonar-large-128k-chat` - Larger chat-only

💡 **Tip**: Online models access real-time web data but count against your quota faster.

## Best Use Cases

- 📚 Looking up latest documentation
- 🔍 Researching current best practices  
- 🐛 Finding solutions to recent issues
- 📰 Getting current library/framework info

## Requirements

- NetBeans 22.0+
- Java 11+
- Perplexity API key (limited free tier)

## Privacy

- Queries sent to Perplexity AI servers
- Code context + web search results processed
- Review privacy policy: https://www.perplexity.ai/privacy

## Pricing

- **Free**: 5 requests/day
- **Pro**: $20/month unlimited requests
- Details: https://www.perplexity.ai/pricing

## Support

- Perplexity Docs: https://docs.perplexity.ai
- API Docs: https://docs.perplexity.ai/docs
- Help: support@perplexity.ai
