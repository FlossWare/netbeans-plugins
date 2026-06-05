# Changelog

All notable changes to the NetBeans AI Plugins Suite will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Fixed
- CI/CD workflow NBM verification count updated from 23 to 27 to match the actual number of NBM-producing modules
- Added CHANGELOG.md for release tracking

## [1.0] - 2026-05-28

### Added
- **AI Core Framework** (`ai/core`): Shared core framework with input validation (MessageValidator) and structured logging
- **Claude AI Plugin** (`ai/claude`): Full-featured integration with Anthropic Claude API (95% test coverage, 49 test files)
- **ChatGPT Plugin** (`ai/chatgpt`): OpenAI GPT integration with code completion, chat, and code explanation actions
- **Gemini Plugin** (`ai/gemini`): Google Gemini AI integration with chat and code context support
- **Grok Plugin** (`ai/grok`): xAI Grok integration for code assistance
- **Mistral Plugin** (`ai/mistral`): Mistral AI integration with streaming support and conversation history
- **Perplexity Plugin** (`ai/perplexity`): Perplexity AI integration with web search capabilities
- **Cohere Plugin** (`ai/cohere`): Cohere enterprise AI platform integration
- **DeepSeek Plugin** (`ai/deepseek`): DeepSeek AI integration for advanced reasoning and coding tasks
- **OpenRouter Plugin** (`ai/openrouter`): OpenRouter gateway providing access to 100+ AI models through a single API
- **14 Language Support Plugins**: Python, Go, Rust, JavaScript, TypeScript, C#, Groovy, BeanShell, MVEL, Bash, PowerShell, Batch, Zsh, Erlang, Ruby, Prolog, Lisp, Kotlin
- **Common Language Framework** (`languages/common`): Shared abstractions for LSP servers, project factories, and settings
- AI-Powered Debugging Suggestions feature
- AI-Powered Test Generation and Javadoc Generation
- Custom exception hierarchy and structured logging
- Retry logic with exponential backoff for API calls
- JaCoCo coverage enforcement across all modules
- Automated CI/CD pipeline with GitHub Actions (build, test, deploy to packagecloud.io, git tagging)
- Automated X.Y versioning with auto-increment
- FREE tier defaults for all AI providers
- GPL v3.0 licensing with headers on all source files
- Code of Conduct and Contributing guidelines
- Comprehensive documentation (32 files across repository and modules)

### Fixed
- Duplicate NBM file generation eliminated
- Process resource leaks in debugger sessions
- CI test failures across Gemini, ChatGPT, Grok, and Python modules
- Duplicate Maven dependencies removed
- Wildcard imports removed from Claude module
- Headless test execution configured for CI environment

### Security
- Zero hardcoded secrets or credentials
- API keys stored in NetBeans secure preference storage
- All external requests use HTTPS
- Input validation on all user-facing API methods
- Process execution properly sanitized
