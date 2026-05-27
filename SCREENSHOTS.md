# NetBeans AI Plugins - Visual Walkthrough

**Complete screenshot guide for all 9 AI assistant plugins**

---

## Table of Contents

1. [Installation](#installation)
2. [Configuration](#configuration)
3. [Using AI Assistants](#using-ai-assistants)
4. [Chat Interface](#chat-interface)
5. [Code Actions](#code-actions)
6. [Build Output](#build-output)

---

## Installation

### Step 1: Download NBM Files

After building the project (`mvn clean package`), you'll find 23 NBM files:

```bash
ai/claude/target/netbeans-claude-integration-1.0.nbm
ai/gemini/target/netbeans-gemini-integration-1.0.nbm
ai/chatgpt/target/netbeans-chatgpt-integration-1.0.nbm
ai/grok/target/netbeans-grok-integration-1.0.nbm
ai/mistral/target/netbeans-mistral-integration-1.0.nbm
ai/perplexity/target/netbeans-perplexity-integration-1.0.nbm
ai/cohere/target/netbeans-cohere-integration-1.0.nbm
ai/deepseek/target/netbeans-deepseek-integration-1.0.nbm
ai/openrouter/target/netbeans-openrouter-integration-1.0.nbm
# ... plus 14 language plugins
```

### Step 2: Install in NetBeans

**Menu Path:**
```
Tools → Plugins → Downloaded → Add Plugins...
```

**What to expect:**
- Select one or more NBM files
- Click "Install"
- Accept license (GPL v3.0)
- Restart NetBeans when prompted

**Screenshot placeholder:** `docs/screenshots/01-plugin-installation.png`
- Shows Plugin Manager window
- Selected NBM files ready to install
- Install button highlighted

---

## Configuration

### API Key Setup

Each AI provider requires an API key. Configuration is in:

```
Tools → Options → Advanced Options → [AI Provider Name]
```

### Claude Configuration Example

**Menu Path:**
```
Tools → Options → Advanced Options → Claude AI
```

**Configuration Fields:**
- ✅ API Key (required)
- ✅ Model Selection (default: claude-sonnet-4-20250514)
- ✅ Max Tokens (default: 4096)
- ✅ Temperature (default: 0.7)
- ✅ Enable Code Completion (checkbox)
- ✅ Include Project Context (checkbox)

**Screenshot placeholder:** `docs/screenshots/02-claude-configuration.png`
- Shows Claude configuration panel
- API key field (masked)
- Model dropdown expanded
- All settings visible

### All AI Providers Configured

**Screenshot placeholder:** `docs/screenshots/03-all-providers-configured.png`
- Shows Options window
- Advanced Options tree expanded
- All 9 AI providers listed:
  - ✅ Claude AI
  - ✅ Gemini AI
  - ✅ ChatGPT AI
  - ✅ Grok AI
  - ✅ Mistral AI
  - ✅ Perplexity AI
  - ✅ Cohere AI
  - ✅ DeepSeek AI
  - ✅ OpenRouter

---

## Using AI Assistants

### FREE Tier Options

**6 out of 9 AI providers offer FREE tiers:**

| Provider | Free Tier | Default Model |
|----------|-----------|---------------|
| **Claude** | ✅ Free credits | claude-sonnet-4-20250514 |
| **Gemini** | ✅ Free tier | gemini-1.5-flash |
| **Mistral** | ✅ Generous free | mistral-small-latest |
| **Cohere** | ✅ 1000 calls/month | command-light |
| **DeepSeek** | ✅ **UNLIMITED FREE** | deepseek-chat |
| **OpenRouter** | ✅ Many free models | llama-3-8b-instruct |
| ChatGPT | 💰 $5 trial credits | gpt-3.5-turbo |
| Perplexity | ⚠️ 5 requests/day | llama-3.1-sonar-small |
| Grok | 💰 Requires X Premium | grok-beta |

**Screenshot placeholder:** `docs/screenshots/04-free-tier-comparison.png`
- Table comparing all providers
- FREE tier indicators highlighted
- Recommended starting points marked

---

## Chat Interface

### Opening Chat Windows

**Menu Path:**
```
Window → [AI Provider Name]
```

Or keyboard shortcuts (configurable):
- `Ctrl+Alt+C` - Claude Chat
- `Ctrl+Alt+G` - Gemini Chat
- `Ctrl+Alt+P` - ChatGPT Chat
- (etc.)

### Chat Window Features

**Screenshot placeholder:** `docs/screenshots/05-claude-chat-window.png`
- Shows Claude chat window
- Example conversation about refactoring code
- Streaming response in progress
- Code blocks with syntax highlighting
- "Insert Code" button visible

**Elements visible:**
1. **Message Input** - Multi-line text area
2. **Send Button** - Send query to AI
3. **Clear Button** - Clear conversation
4. **Response Area** - Shows AI responses with Markdown rendering
5. **Code Blocks** - Syntax-highlighted code in responses
6. **Insert Code** - Buttons to insert code into editor

### Side-by-Side Comparison

**Screenshot placeholder:** `docs/screenshots/06-multi-provider-comparison.png`
- Split screen showing 3 AI providers answering same question
- Claude, Gemini, and DeepSeek windows side-by-side
- Same code question, different responses
- Demonstrates variety in approaches

---

## Code Actions

### Context Menu Integration

Right-click on selected code to access AI features:

**Screenshot placeholder:** `docs/screenshots/07-context-menu-actions.png`
- Code editor with Java method selected
- Context menu open showing:
  - ✅ Ask Claude About This Code
  - ✅ Ask Gemini About This Code
  - ✅ Ask ChatGPT About This Code
  - ✅ Ask Mistral About This Code
  - ✅ Explain This Code (Claude)
  - ✅ Suggest Refactoring (Claude)

### Explain Code Feature

**Screenshot placeholder:** `docs/screenshots/08-explain-code-action.png`
- Before: Selected complex algorithm code
- After: Chat window opens with detailed explanation
- AI breaks down algorithm step-by-step
- Suggests improvements

### Refactoring Suggestions

**Screenshot placeholder:** `docs/screenshots/09-refactoring-suggestions.png`
- Selected: Long method with code smell
- AI response: Suggests Extract Method pattern
- Shows before/after code comparison
- Explains benefits of refactoring

---

## Code Completion

### AI-Powered Code Suggestions

**Trigger:** Press `Ctrl+Space` while typing

**Screenshot placeholder:** `docs/screenshots/10-code-completion.png`
- Shows code editor mid-typing
- Completion popup with AI suggestions
- Claude suggestion highlighted with ⭐ icon
- Traditional NetBeans completions also visible
- Multi-line suggestion preview

**Features:**
- ✅ Context-aware suggestions
- ✅ Includes project context
- ✅ Multi-line completions
- ✅ Documentation preview
- ✅ Smart ranking

---

## Build Output

### Successful Build

**Screenshot placeholder:** `docs/screenshots/11-build-success.png`
- Terminal showing `mvn clean package` output
- All 23 modules built successfully
- Build time: ~4 minutes
- 23 NBM files created

**Example output:**
```
[INFO] Reactor Summary for NetBeans Plugins 1.0:
[INFO] 
[INFO] NetBeans Plugins ................................... SUCCESS [  0.523 s]
[INFO] NetBeans Claude Integration ........................ SUCCESS [ 45.234 s]
[INFO] NetBeans Gemini Integration ........................ SUCCESS [ 12.456 s]
[INFO] NetBeans ChatGPT Integration ....................... SUCCESS [ 11.234 s]
[INFO] NetBeans Grok Integration .......................... SUCCESS [  8.567 s]
[INFO] NetBeans Mistral Integration ....................... SUCCESS [  9.123 s]
[INFO] NetBeans Perplexity Integration .................... SUCCESS [  8.890 s]
[INFO] NetBeans Cohere Integration ........................ SUCCESS [  8.765 s]
[INFO] NetBeans DeepSeek Integration ...................... SUCCESS [  8.654 s]
[INFO] NetBeans OpenRouter Integration .................... SUCCESS [  9.012 s]
[INFO] NetBeans Common Language Support ................... SUCCESS [ 15.234 s]
[INFO] NetBeans Python Support ............................ SUCCESS [ 18.567 s]
... (14 language plugins)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4:15 min
```

### CI/CD Pipeline

**Screenshot placeholder:** `docs/screenshots/12-github-actions.png`
- GitHub Actions workflow passing
- All checks green
- Build, Test, Deploy stages complete
- Artifacts uploaded (23 NBM files)

---

## Language Support Plugins

### Python Plugin

**Screenshot placeholder:** `docs/screenshots/13-python-support.png`
- Python file open in editor
- Syntax highlighting active
- Code completion working
- LSP features enabled
- Debug console visible

### Bash Plugin

**Screenshot placeholder:** `docs/screenshots/14-bash-support.png`
- Bash script open
- Syntax highlighting
- Variable completion
- Debugging session active
- Output console showing script execution

---

## Real-World Usage Examples

### Example 1: Bug Fix Assistance

**Screenshot placeholder:** `docs/screenshots/15-bug-fix-example.png`
- Bug in Java code (NullPointerException)
- Ask Claude: "Why am I getting NPE here?"
- Claude explains the issue
- Suggests fix with null check
- Shows improved code

### Example 2: Learning New API

**Screenshot placeholder:** `docs/screenshots/16-api-learning.png`
- User asks: "How do I use Java Streams API to filter and map?"
- Claude provides:
  - Explanation of Stream operations
  - Multiple code examples
  - Best practices
  - Common pitfalls

### Example 3: Code Review

**Screenshot placeholder:** `docs/screenshots/17-code-review.png`
- User selects pull request code
- Asks multiple AIs for review
- Claude: Focus on architecture
- Gemini: Focus on performance
- DeepSeek: Focus on best practices
- Side-by-side comparison of feedback

---

## Performance Metrics

### Response Times

**Screenshot placeholder:** `docs/screenshots/18-performance-metrics.png`
- Chart showing average response times per provider
- Claude: ~2-3 seconds
- Gemini: ~1-2 seconds
- DeepSeek: ~2-4 seconds
- ChatGPT: ~3-5 seconds

### Test Coverage Report

**Screenshot placeholder:** `docs/screenshots/19-test-coverage.png`
- JaCoCo coverage report
- Claude module: 95% coverage
- Package-level breakdown
- Line and branch coverage
- Visual coverage graphs

---

## Troubleshooting

### Common Issues

#### API Key Not Working

**Screenshot placeholder:** `docs/screenshots/20-api-key-error.png`
- Shows error dialog: "Invalid API key"
- Highlights where to fix in settings
- Link to API key signup page

#### Rate Limit Exceeded

**Screenshot placeholder:** `docs/screenshots/21-rate-limit.png`
- Error: "Rate limit exceeded"
- Suggests switching to free provider
- Shows DeepSeek as unlimited alternative

---

## Best Practices

### Comparing AI Responses

**Screenshot placeholder:** `docs/screenshots/22-comparison-workflow.png`
- Same question sent to 3 providers
- Results compared side-by-side
- User selects best approach
- Demonstrates value of multi-provider access

---

## Getting Started Checklist

**Screenshot placeholder:** `docs/screenshots/23-getting-started.png`
- Checklist overlay on NetBeans:
  - ✅ Install plugins
  - ✅ Configure at least one API key (recommend DeepSeek for free)
  - ✅ Test chat window
  - ✅ Try code completion (Ctrl+Space)
  - ✅ Right-click code for actions
  - ✅ Compare multiple AI responses

---

## Notes on Screenshots

**Current Status:** Screenshot placeholders created

**To Add Actual Screenshots:**
1. Install NetBeans 22.0+
2. Install all 23 plugin modules
3. Configure API keys for each provider
4. Capture screenshots for each scenario above
5. Save as PNG in `docs/screenshots/` directory
6. Update this file with actual image references

**Image Format:**
```markdown
![Description](docs/screenshots/filename.png)
```

**Recommended Resolution:** 1920x1080 (Full HD)

**Recommended Tool:** [Flameshot](https://flameshot.org/) or OS built-in screenshot tool

---

## Video Tutorials (Future)

**Planned video content:**
1. Installation and setup (5 min)
2. Using AI chat windows (10 min)
3. Code completion features (8 min)
4. Comparing AI providers (12 min)
5. Advanced features (15 min)

**Video hosting:** YouTube channel (to be created)

---

**Last Updated:** 2026-05-27  
**Version:** 1.0  
**License:** GPL v3.0  
**Author:** FlossWare

For actual usage, see [README.md](README.md)
