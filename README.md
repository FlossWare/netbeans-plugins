# FlossWare NetBeans AI Assistant Suite

**The industry's first vendor-neutral, multi-provider AI platform for NetBeans IDE**

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Build Status](https://github.com/FlossWare/netbeans-plugins/workflows/CD-CI/badge.svg)](https://github.com/FlossWare/netbeans-plugins/actions)
[![NetBeans](https://img.shields.io/badge/NetBeans-22.0+-orange.svg)](https://netbeans.apache.org/)
[![Java](https://img.shields.io/badge/Java-11+-red.svg)](https://openjdk.java.net/)

---

## 🚀 Transform Your Development Workflow

Boost your productivity with AI-powered code completion and intelligent assistance—all while maintaining complete freedom to choose from **9 leading AI providers** with **zero vendor lock-in**. Advanced features like automated test generation, documentation, and debugging are available in Claude and coming to additional providers.

### Why FlossWare?

| **What Sets Us Apart** | **The Value** |
|------------------------|---------------|
| 🌐 **9 AI Providers** | Claude, Gemini, ChatGPT, Grok, Mistral, Perplexity, Cohere, DeepSeek, OpenRouter |
| 💰 **6 Free Tiers + Unlimited Free** | Zero-cost evaluation and unlimited DeepSeek access |
| ⚡ **Productivity Gains** | Potential gains with Claude's advanced features (test generation, debugging, documentation)* |
| 🔓 **Zero Vendor Lock-In** | Switch providers anytime based on cost, performance, or compliance |
| 🧪 **Automated Test Generation** | JUnit 5 + Mockito + AssertJ in seconds **(Claude)**  |
| 📚 **AI-Powered Javadoc** | Comprehensive documentation from code analysis **(Claude)** |
| 🐛 **Intelligent Debug Assist** | Error analysis with fix suggestions **(Claude)** |
| 📖 **100% Open Source** | Full transparency, auditability, and customization |
| ⚙️ **5-Minute Setup** | Install, configure API key, start coding |
| 💼 **Enterprise-Ready** | SOC2/GDPR/HIPAA compliance through provider choice |

### Potential ROI

> **Disclaimer:** The figures below are hypothetical projections assuming a 10-developer team at $100K average salary. They are derived from general industry benchmarks for AI-assisted development, **not** from pilot studies, customer case studies, or controlled experiments conducted with this software. Actual results will vary significantly based on team composition, project type, AI provider chosen, and usage patterns.

- Estimated annual savings on routine tasks: **$200K -- $500K** (range depends on adoption depth)
- Estimated first-year ROI: **several hundred to several thousand percent**
- Estimated payback period: **days to weeks**

*See [projection methodology and assumptions](EXECUTIVE_SUMMARY.md#quantified-business-impact) for the detailed calculations behind these ranges.*

---

## 🎯 Perfect For

✅ **Enterprise Java Teams** - NetBeans-native with compliance flexibility  
✅ **Startups & SMBs** - Free tiers eliminate budget constraints  
✅ **Educational Institutions** - Unlimited free access for students  
✅ **Individual Developers** - Professional AI tools without subscriptions  
✅ **Regulated Industries** - Open source + provider choice = compliance  

---

## Overview

⚠️ **Maturity Notice:** The Claude plugin is production-ready with 95% test coverage and 573 tests. Other AI plugins are functional but experimental (<10% coverage each). Language plugins are early-stage implementations.

### AI Assistant Plugins

Nine NetBeans plugins integrating popular AI providers (6 with FREE tiers!):

**FREE Tier Available ✅**
- **Claude** - Anthropic's Claude AI (✅ **Production-Ready: 95% coverage, 573 tests**)
- **Gemini** - Google's Gemini AI (⚠️ Experimental: <10% coverage)
- **Mistral** - Mistral AI (⚠️ Experimental: <10% coverage)
- **Cohere** - Enterprise AI (⚠️ Experimental: <10% coverage)
- **DeepSeek** - 🌟 Completely FREE (⚠️ Experimental: <10% coverage)
- **OpenRouter** - Access to 100+ models (⚠️ Experimental: <10% coverage)

**Paid/Limited 💰**
- **ChatGPT** - OpenAI's GPT (⚠️ Experimental: <10% coverage)
- **Perplexity** - Search-focused AI (⚠️ Experimental: <10% coverage)
- **Grok** - xAI's Grok (⚠️ Experimental: <10% coverage)

### Language Support Plugins

⚠️ **Early Stage:** Language plugins provide basic syntax highlighting and structure. LSP integration is experimental and not fully tested.

**Core:**
- **Common** - Shared abstractions and utilities for language plugins
- **Python** - Python language support using Language Server Protocol (LSP)
- **Groovy** - Groovy scripting language support with LSP
- **BeanShell** - BeanShell Java scripting support
- **MVEL** - MVEL expression language support

**Shell Scripting:**
- **Bash** - Bash shell script support with debugging
- **Zsh** - Z shell script support with debugging
- **PowerShell** - Windows PowerShell support with debugging
- **Batch** - Windows Batch file support with debugging

**Programming Languages:**
- **Erlang** - Erlang functional programming support
- **Ruby** - Ruby language support with debugging
- **Kotlin** - Kotlin JVM language support
- **Prolog** - Prolog logic programming support
- **Lisp** - Lisp functional programming support

All plugins can be installed simultaneously without conflicts.

---

## 💎 Why Choose FlossWare Over Alternatives?

FlossWare is the **only solution** offering enterprise-grade, multi-provider AI assistance for NetBeans with complete vendor neutrality.

| Criterion | FlossWare | Single-Vendor Alternatives |
|-----------|-----------|---------------------------|
| **AI Providers** | **9 providers** | 1 per tool |
| **Free Options** | **6 free tiers + unlimited DeepSeek** | None or limited |
| **NetBeans Support** | **Native** | Limited or none |
| **Open Source** | **Full** | Proprietary or partial |
| **Vendor Lock-In** | **None** | High |
| **Advanced Features** | **Test gen, Javadoc, Debug (Claude)** | Code completion only |

[**See Full Competitive Analysis -->**](VALUE_PROPOSITION.md#competitive-landscape)

---

## Features by Provider Availability

### Universal Features (All 9 Providers)

✨ **AI-Powered Code Completion** - Intelligent suggestions via Ctrl+Space  
💬 **Interactive Chat Window** - Dedicated chat interface with streaming responses  
⚡ **Editor Integration** - Right-click actions for code explanation and refactoring  
📝 **Code Insertion** - Automatic detection and insertion of code blocks  
🗂️ **Project Context** - Includes project structure in queries  
⚙️ **Configuration Panel** - Full control over API settings and behavior  

### Advanced Features (Claude Only)

🧪 **Automated Test Generation** - AI-powered test generation (JUnit 5 + Mockito + AssertJ)  
📚 **Javadoc Generation** - Auto-generate comprehensive documentation  
🐛 **Intelligent Debug Assist** - AI-powered error analysis and fix suggestions  

**Note:** These advanced features are currently only available with the Claude provider. Gemini, ChatGPT, Grok, Mistral, Perplexity, Cohere, DeepSeek, and OpenRouter plugins support the universal features above. We're actively working on expanding advanced features to additional providers.

### Provider Capability Matrix

| Feature | Claude | Gemini | ChatGPT | Grok | Mistral | Perplexity | Cohere | DeepSeek | OpenRouter |
|---------|--------|--------|---------|------|---------|------------|--------|----------|------------|
| Code Completion | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Chat Interface | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Code Explanation | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Code Refactoring | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| **Test Generation** | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ |
| **Javadoc Generation** | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ |
| **Debug Assistance** | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Production-Ready | ✅ | ⚠️ | ⚠️ | ⚠️ | ⚠️ | ⚠️ | ⚠️ | ⚠️ | ⚠️ |  

## Quick Start

### Build All Plugins

```bash
mvn clean package
```

This builds all plugins in one command (23 total):

**AI Plugins (9 modules):**
- `ai/claude/target/netbeans-claude-integration-1.0.nbm`
- `ai/gemini/target/netbeans-gemini-integration-1.0.nbm`
- `ai/chatgpt/target/netbeans-chatgpt-integration-1.0.nbm`
- `ai/grok/target/netbeans-grok-integration-1.0.nbm`
- `ai/mistral/target/netbeans-mistral-integration-1.0.nbm` ✨ NEW
- `ai/perplexity/target/netbeans-perplexity-integration-1.0.nbm` ✨ NEW
- `ai/cohere/target/netbeans-cohere-integration-1.0.nbm` ✨ NEW
- `ai/deepseek/target/netbeans-deepseek-integration-1.0.nbm` ✨ NEW
- `ai/openrouter/target/netbeans-openrouter-integration-1.0.nbm` ✨ NEW

**Language Plugins (14 modules):**
- `languages/common/target/netbeans-common-1.0.nbm`
- `languages/python/target/netbeans-python-1.0.nbm`
- `languages/groovy/target/netbeans-groovy-1.0.nbm`
- `languages/beanshell/target/netbeans-beanshell-1.0.nbm`
- `languages/mvel/target/netbeans-mvel-1.0.nbm`
- `languages/bash/target/netbeans-bash-support-1.0.nbm`
- `languages/zsh/target/netbeans-zsh-support-1.0.nbm`
- `languages/powershell/target/netbeans-powershell-support-1.0.nbm`
- `languages/batch/target/netbeans-batch-support-1.0.nbm`
- `languages/erlang/target/netbeans-erlang-support-1.0.nbm`
- `languages/ruby/target/netbeans-ruby-support-1.0.nbm`
- `languages/kotlin/target/netbeans-kotlin-support-1.0.nbm`
- `languages/prolog/target/netbeans-prolog-support-1.0.nbm`
- `languages/lisp/target/netbeans-lisp-support-1.0.nbm`

### Build Individual Plugin

```bash
# AI Assistants
mvn clean package -pl ai/claude
mvn clean package -pl ai/gemini
mvn clean package -pl ai/chatgpt
mvn clean package -pl ai/grok
mvn clean package -pl ai/mistral
mvn clean package -pl ai/perplexity
mvn clean package -pl ai/cohere
mvn clean package -pl ai/deepseek
mvn clean package -pl ai/openrouter

# Language Support - Core
mvn clean package -pl languages/common
mvn clean package -pl languages/python
mvn clean package -pl languages/groovy
mvn clean package -pl languages/beanshell
mvn clean package -pl languages/mvel

# Language Support - Shell Scripting
mvn clean package -pl languages/bash
mvn clean package -pl languages/zsh
mvn clean package -pl languages/powershell
mvn clean package -pl languages/batch

# Language Support - Programming Languages
mvn clean package -pl languages/erlang
mvn clean package -pl languages/ruby
mvn clean package -pl languages/kotlin
mvn clean package -pl languages/prolog
mvn clean package -pl languages/lisp

# Build all AI plugins only
mvn clean package -pl ai/claude,ai/gemini,ai/chatgpt,ai/grok,ai/mistral,ai/perplexity,ai/cohere,ai/deepseek,ai/openrouter

# Build all language plugins only
mvn clean package -pl languages/common,languages/python,languages/groovy,languages/beanshell,languages/mvel,languages/bash,languages/zsh,languages/powershell,languages/batch,languages/erlang,languages/ruby,languages/kotlin,languages/prolog,languages/lisp
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
- **NetBeans**: 22.0+ (RELEASE220 or higher)
- **API Keys**:
  - Claude: https://console.anthropic.com/
  - Gemini: https://makersuite.google.com/app/apikey
  - ChatGPT: https://console.openai.com/
  - Grok: https://console.x.ai/
  - Mistral: https://console.mistral.ai/
  - Perplexity: https://www.perplexity.ai/settings/api
  - Cohere: https://dashboard.cohere.com/
  - DeepSeek: https://platform.deepseek.com/
  - OpenRouter: https://openrouter.ai/keys

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

### 📋 Strategic & Business

- **[EXECUTIVE_SUMMARY.md](EXECUTIVE_SUMMARY.md)** - 🌟 **Comprehensive business case, ROI analysis, and strategic value**
- **[VALUE_PROPOSITION.md](VALUE_PROPOSITION.md)** - Positioning, messaging, competitive analysis, and market opportunity
- **[CRITICAL_ASSESSMENT.md](CRITICAL_ASSESSMENT.md)** - Honest project review and improvement roadmap

### 📖 General

- **[BUILD_STATUS.md](BUILD_STATUS.md)** - Maven build issues and solutions
- **[PLUGINS_SUMMARY.md](PLUGINS_SUMMARY.md)** - Feature comparison across all plugins
- **[NETBEANS_PLUGINS_OVERVIEW.md](NETBEANS_PLUGINS_OVERVIEW.md)** - Complete project overview
- **[TEST_COVERAGE_SUMMARY.md](TEST_COVERAGE_SUMMARY.md)** - Test coverage report (Claude: 95%, Others: <10%)
- **[TESTING.md](TESTING.md)** - Testing guide and best practices
- **[VERSIONING.md](VERSIONING.md)** - Version format (X.Y) and auto-versioning system
- **[SCREENSHOTS.md](SCREENSHOTS.md)** - Visual walkthrough with screenshots 📸
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - Contribution guidelines for developers 🤝

### AI Plugins

- **[ai/claude/README.md](ai/claude/README.md)** - Claude plugin documentation (production-ready)
- **[ai/gemini/README.md](ai/gemini/README.md)** - Gemini plugin documentation
- **[ai/chatgpt/README.md](ai/chatgpt/README.md)** - ChatGPT plugin documentation
- **[ai/grok/README.md](ai/grok/README.md)** - Grok plugin documentation
- **[ai/mistral/README.md](ai/mistral/README.md)** - Mistral plugin documentation
- **[ai/perplexity/README.md](ai/perplexity/README.md)** - Perplexity plugin documentation
- **[ai/cohere/README.md](ai/cohere/README.md)** - Cohere plugin documentation
- **[ai/deepseek/README.md](ai/deepseek/README.md)** - DeepSeek plugin documentation
- **[ai/openrouter/README.md](ai/openrouter/README.md)** - OpenRouter plugin documentation

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
- **Tools → Options → Advanced Options → Grok AI**
- **Tools → Options → Advanced Options → Mistral AI**
- **Tools → Options → Advanced Options → Perplexity AI**
- **Tools → Options → Advanced Options → Cohere AI**
- **Tools → Options → Advanced Options → DeepSeek AI**
- **Tools → Options → Advanced Options → OpenRouter**

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

---

## 📈 Potential Productivity Impact & Business Value

> **Important:** All figures in this section are **hypothetical projections** based on general industry benchmarks for AI-assisted development tools. They assume a best-case adoption scenario and have **not** been validated through pilot studies, controlled experiments, or documented customer deployments using this software. Individual results will vary significantly depending on project complexity, developer experience, AI provider chosen, and usage patterns.

### Hypothetical Time Savings (Claude Module)

| Development Task | Typical Time | Potential Time With AI | Potential Gain |
|------------------|-------------|--------------|-------------------|
| **Writing Unit Tests** | 45 min | 10--25 min | **~2x--4.5x faster** |
| **Debugging Errors** | 2.5 hours | 1--1.5 hours | **~1.5x--2.5x faster** |
| **Code Documentation** | 30 min | 5--15 min | **~2x--6x faster** |
| **Understanding Legacy Code** | 4 hours | 2--3 hours | **~1.3x--2x faster** |
| **Boilerplate Coding** | 60 min | 15--30 min | **~2x--4x faster** |

**Hypothetical average: ~1.5x--3x productivity improvement** across common tasks (range reflects varying team and project conditions)

### Hypothetical ROI (10-Developer Team @ $100K/year)

| Category | Hypothetical Annual Impact |
|----------|---------------|
| **Developer time saved** | +$200K -- $500K |
| **AI costs (mixed free/paid)** | -$12K -- $24K |
| **Reduced bug fixing** | +$30K -- $75K |
| **Faster delivery value** | +$50K -- $200K |
| **Hypothetical Net Annual Benefit** | **+$268K -- $751K** |

These ranges reflect different levels of adoption, team composition, and project complexity. The low end assumes modest adoption with simpler projects; the high end assumes full adoption across a team working on complex enterprise codebases.

**No independent studies or customer case studies have been conducted to validate these projections.**

📊 **[See Full Projection Methodology & Assumptions →](EXECUTIVE_SUMMARY.md#quantified-business-impact)**

---

## Statistics

### AI Plugins

- **Modules**: 9 total (1 production-ready, 8 experimental)
- **Claude (Production)**: 40 classes, ~5,000 LOC, 95% coverage, 573 tests
- **Others (Experimental)**: 23 classes each, ~4,000 LOC each, <10% coverage
- **Note:** ⚠️ Significant code duplication across modules (see CRITICAL_ASSESSMENT.md)
- **Recommendation:** Use Claude plugin for production; others for experimentation only

### Language Plugins

- **Total Modules**: 14
- **Core Modules**: 5 (Common, Python, Groovy, BeanShell, MVEL)
- **Shell Scripts**: 4 (Bash, Zsh, PowerShell, Batch)
- **Programming Languages**: 5 (Erlang, Ruby, Kotlin, Prolog, Lisp)
- **Features**: Syntax highlighting, code completion, debugging support
- **Test Coverage**: Basic infrastructure tests

### Total Project

- **Total Modules**: 23 (9 AI + 14 Language)
- **Total Test Files**: 114
- **Total Tests**: 573 (across all modules)
- **Overall Test Coverage**: 95% (Claude module with advanced features)
- **Build Time**: ~3 minutes (with tests)
- **Can Install Together**: ✅ Yes, all plugins
- **Conflicts**: ❌ None

## Why Multi-Module?

✅ **Single build command** - Build all at once  
✅ **Shared configuration** - Common dependencies in parent POM  
✅ **Consistent versioning** - All modules use same version  
✅ **Easier maintenance** - Change once, applies to all  
✅ **Logical grouping** - Clear they're a family  
✅ **Better CI/CD** - One pipeline for all  

## Testing

### Test Coverage Status ⚠️

**IMPORTANT:** Test coverage varies significantly across modules. Only the Claude module has comprehensive test coverage.

**Claude Module (Production-Ready):**
- **Overall**: 95% line coverage (573 tests, 2792/2927 lines)
- **actions**: 100% (includes Test & Javadoc generation)
- **api**: 97% (312/320 lines)
- **completion**: 95% (751/784 lines)
- **options**: 99% (710/716 lines)
- **ui**: 95% (516/538 lines)
- **util**: 93% (161/172 lines)
- **context**: 76% (requires NetBeans platform runtime)

**Other AI Modules (Experimental - Low Coverage):**
- Gemini, ChatGPT, Grok, Mistral, Perplexity, Cohere, DeepSeek, OpenRouter: <10% coverage each
- Minimal tests (basic construction and API tests only)
- **Status: Functional but not production-ready**

**Language Modules (Early Stage):**
- Basic infrastructure tests only
- LSP integration not fully tested
- **Status: Experimental - use with caution**

**Test Infrastructure:**
- JUnit 5 for test framework
- Mockito for mocking
- AssertJ for fluent assertions
- MockWebServer for HTTP testing
- JaCoCo for coverage reporting

### Run Tests

```bash
# All tests
mvn clean test

# With coverage report
mvn clean test jacoco:report

# Specific module
mvn test -pl ai/claude

# View coverage report (after running tests)
open ai/claude/target/site/jacoco/index.html
```

### CI/CD

Tests run automatically on every push via GitHub Actions. Build fails if:
- Any test fails
- Coverage drops below 60% (current: 95% - exceeds threshold)

See **[TEST_COVERAGE_SUMMARY.md](TEST_COVERAGE_SUMMARY.md)** for detailed coverage breakdown.

## Versioning

### Version Format: X.Y

This project uses **X.Y** semantic versioning (e.g., `1.0`, `1.1`, `2.0`):
- **X**: Major version (breaking changes)
- **Y**: Minor version (features, fixes)

**Current Version**: `1.0`

### Auto-Versioning

Every push to `main` automatically:
1. Increments minor version (`1.0` → `1.1`)
2. Builds all 18 modules
3. Runs 573 tests
4. Deploys to PackageCloud
5. Commits version bump
6. Creates git tag

See **[VERSIONING.md](VERSIONING.md)** for complete details.

### Manual Version Bump

```bash
# Increment minor version (1.0 → 1.1)
mvn build-helper:parse-version versions:set \
  -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion} \
  versions:commit

# Increment major version (1.9 → 2.0)
mvn versions:set -DnewVersion=2.0 versions:commit
```

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
