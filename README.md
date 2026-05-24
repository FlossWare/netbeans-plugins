# NetBeans Plugins

Multi-module Maven project providing comprehensive NetBeans IDE plugins for **AI assistants** and **language support**.

## Overview

### AI Assistant Plugins
Three complete NetBeans plugins with identical features, each integrating a different AI provider:
- **Claude** - Anthropic's Claude AI
- **Gemini** - Google's Gemini AI
- **ChatGPT** - OpenAI's ChatGPT

### Language Support Plugins
LSP-based language plugins for multi-language development:
- **Common** - Shared abstractions and utilities for language plugins
- **Python** - Python language support using Language Server Protocol (LSP)
- **Groovy** - Groovy scripting language support with LSP
- **BeanShell** - BeanShell Java scripting support
- **MVEL** - MVEL expression language support

All plugins can be installed simultaneously without conflicts.

## Features (All Plugins)

✨ **AI-Powered Code Completion** - Intelligent suggestions via Ctrl+Space  
💬 **Interactive Chat Window** - Dedicated chat interface with streaming responses  
⚡ **Editor Integration** - Right-click actions for code explanation and refactoring  
📝 **Code Insertion** - Automatic detection and insertion of code blocks  
🗂️ **Project Context** - Includes project structure in queries  
⚙️ **Configuration Panel** - Full control over API settings and behavior  

## Quick Start

### Build All Plugins

```bash
mvn clean package
```

This builds all plugins in one command:
- `ai/claude/target/netbeans-claude-integration-1.0.0-SNAPSHOT.nbm`
- `ai/gemini/target/netbeans-gemini-integration-1.0.0-SNAPSHOT.nbm`
- `ai/chatgpt/target/netbeans-chatgpt-integration-1.0.0-SNAPSHOT.nbm`
- `languages/common/target/netbeans-common-1.0.0-SNAPSHOT.nbm`
- `languages/python/target/netbeans-python-1.0.0-SNAPSHOT.nbm`
- `languages/groovy/target/netbeans-groovy-1.0.0-SNAPSHOT.nbm`
- `languages/beanshell/target/netbeans-beanshell-1.0.0-SNAPSHOT.nbm`
- `languages/mvel/target/netbeans-mvel-1.0.0-SNAPSHOT.nbm`

### Build Individual Plugin

```bash
# AI Assistants
mvn clean package -pl ai/claude
mvn clean package -pl ai/gemini
mvn clean package -pl ai/chatgpt

# Language Support
mvn clean package -pl languages/common
mvn clean package -pl languages/python
mvn clean package -pl languages/groovy
mvn clean package -pl languages/beanshell
mvn clean package -pl languages/mvel

# Build all AI plugins only
mvn clean package -pl ai/claude,ai/gemini,ai/chatgpt

# Build all language plugins only
mvn clean package -pl languages/common,languages/python,languages/groovy,languages/beanshell,languages/mvel
```

### Install in NetBeans

1. Tools → Plugins → Downloaded
2. Add Plugins... → Select one or more NBM files
3. Install and restart NetBeans
4. Tools → Options → Advanced Options → Configure each AI

## Module Structure

```
netbeans-plugins/
├── pom.xml                    # Parent POM with shared config
├── README.md                  # This file
├── BUILD_STATUS.md            # Build troubleshooting
├── PLUGINS_SUMMARY.md         # Feature comparison
├── ai/                        # AI Assistant Plugins
│   ├── claude/                # Claude plugin module
│   │   ├── pom.xml
│   │   ├── README.md
│   │   └── src/...
│   ├── gemini/                # Gemini plugin module
│   │   ├── pom.xml
│   │   ├── README.md
│   │   └── src/...
│   └── chatgpt/               # ChatGPT plugin module
│       ├── pom.xml
│       ├── README.md
│       └── src/...
└── languages/                 # Language Support Plugins
    ├── common/                # Shared language utilities
    │   ├── pom.xml
    │   ├── README.md
    │   └── src/...
    ├── python/                # Python LSP plugin
    │   ├── pom.xml
    │   ├── README.md
    │   └── src/...
    ├── groovy/                # Groovy LSP plugin
    │   ├── pom.xml
    │   ├── README.md
    │   └── src/...
    ├── beanshell/             # BeanShell plugin
    │   ├── pom.xml
    │   ├── README.md
    │   └── src/...
    └── mvel/                  # MVEL plugin
        ├── pom.xml
        ├── README.md
        └── src/...
```

## Prerequisites

- **Java**: 11+
- **Maven**: 3.6+
- **NetBeans**: 24.0+ (RELEASE220)
- **API Keys**:
  - Claude: https://console.anthropic.com/
  - Gemini: https://makersuite.google.com/app/apikey
  - ChatGPT: https://console.openai.com/

## Technology Stack

### Common (All Modules)
- NetBeans Platform APIs (RELEASE220)
- Maven with nbm-maven-plugin
- Async processing (RequestProcessor)
- Smart caching (TTL-based)
- Gson for JSON

### Module-Specific

**Claude:**
- Anthropic SDK (official Java client)

**Gemini:**
- Google Cloud AI Platform SDK
- OkHttp for REST API

**ChatGPT:**
- OpenAI GPT3 Java SDK (theokanning)
- RxJava2 for streaming

## Documentation

### General
- **[BUILD_STATUS.md](BUILD_STATUS.md)** - Maven build issues and solutions
- **[PLUGINS_SUMMARY.md](PLUGINS_SUMMARY.md)** - Feature comparison across all plugins
- **[NETBEANS_PLUGINS_OVERVIEW.md](NETBEANS_PLUGINS_OVERVIEW.md)** - Complete project overview

### AI Plugins
- **[ai/claude/README.md](ai/claude/README.md)** - Claude plugin documentation
- **[ai/gemini/README.md](ai/gemini/README.md)** - Gemini plugin documentation
- **[ai/chatgpt/README.md](ai/chatgpt/README.md)** - ChatGPT plugin documentation

### Language Plugins
- **[languages/common/README.md](languages/common/README.md)** - Shared language support utilities
- **[languages/python/README.md](languages/python/README.md)** - Python LSP integration
- **[languages/groovy/README.md](languages/groovy/README.md)** - Groovy language support
- **[languages/beanshell/README.md](languages/beanshell/README.md)** - BeanShell scripting support
- **[languages/mvel/README.md](languages/mvel/README.md)** - MVEL expression language support

## Configuration

Each plugin has its own settings panel:
- **Tools → Options → Advanced Options → Claude AI**
- **Tools → Options → Advanced Options → Gemini AI**
- **Tools → Options → Advanced Options → ChatGPT AI**

Configure:
- API key (stored securely)
- Model selection
- Temperature and max tokens
- Code completion settings
- Project context options

## Usage

### Code Completion
1. Type in any Java file
2. Press **Ctrl+Space**
3. Select AI suggestion
4. Press **Enter**

### Chat Interface
1. **Tools → Open [AI] Chat**
2. Type your question
3. View streaming response
4. Click **Insert Code** to use suggestions

### Code Actions
Right-click on selected code:
- Ask [AI] About This Code
- Explain This Code ([AI])
- Suggest Refactoring ([AI])

## Statistics

### AI Plugins (per plugin)
- **Java Classes**: ~23
- **Lines of Code**: ~4,000
- **Packages**: 7
- **Features**: 6

### Language Plugins
- **Common Module**: Shared abstractions
- **Python Module**: LSP-based Python support
- **Groovy Module**: LSP-based Groovy support
- **BeanShell Module**: BeanShell scripting support
- **MVEL Module**: MVEL expression language support

### Total Project
- **Modules**: 8 (3 AI + 5 Language)
- **Can Install Together**: ✅ Yes
- **Conflicts**: ❌ None

## Why Multi-Module?

✅ **Single build command** - Build all at once  
✅ **Shared configuration** - Common dependencies in parent POM  
✅ **Consistent versioning** - All modules use same version  
✅ **Easier maintenance** - Change once, applies to all  
✅ **Logical grouping** - Clear they're a family  
✅ **Better CI/CD** - One pipeline for all  

## Development

### Add Dependency to All Modules

Add to parent `<dependencyManagement>`:
```xml
<dependency>
    <groupId>...</groupId>
    <artifactId>...</artifactId>
    <version>...</version>
</dependency>
```

Then reference in module POMs without version:
```xml
<dependency>
    <groupId>...</groupId>
    <artifactId>...</artifactId>
</dependency>
```

### Update Version

```bash
mvn versions:set -DnewVersion=1.0.0
```

Updates parent and all modules.

## Troubleshooting

### Build Fails with Missing NetBeans Dependencies

See **BUILD_STATUS.md** for solutions to corporate Maven mirror issues.

### Individual Module Won't Build

```bash
# Build parent first
mvn clean install -N

# Then build module
cd ai/claude
mvn clean package

# Or for language plugins
cd languages/python
mvn clean package
```

## License

Apache License 2.0

## Credits

**Author**: FlossWare  
**AI Assistant**: Claude Sonnet 4.5  
**Architecture**: Shared across all modules with AI-specific adaptations
