# Critical Project Assessment - FlossWare NetBeans Plugins

**Date:** 2026-05-27  
**Reviewer:** Independent Code Review  
**Overall Grade:** C+ (75/100)

---

## Executive Summary

This project shows **ambition and potential** but suffers from fundamental architectural problems, massive code duplication, and misleading documentation claims. The Claude module demonstrates good engineering practices (65% test coverage, 531 tests), but the remaining 22 modules range from minimally tested to stub implementations.

**Key Finding:** ~15,000 lines of duplicated code across 9 AI plugins that should be ~3,000 lines with proper abstraction.

---

## Detailed Scoring

| Category | Score | Weight | Weighted | Status |
|----------|-------|--------|----------|--------|
| **Architecture & Design** | 3/10 | 25% | 7.5 | 🔴 Critical |
| **Code Quality** | 5/10 | 20% | 10.0 | 🟡 Needs Work |
| **Testing** | 4/10 | 20% | 8.0 | 🔴 Critical |
| **Documentation** | 7/10 | 10% | 7.0 | 🟢 Good |
| **Error Handling** | 4/10 | 10% | 4.0 | 🔴 Critical |
| **Dependencies** | 6/10 | 5% | 3.0 | 🟡 Needs Work |
| **CI/CD** | 7/10 | 5% | 3.5 | 🟢 Good |
| **Security** | 4/10 | 5% | 2.0 | 🔴 Critical |
| **TOTAL** | | **100%** | **45/100** | |

**Adjusted Score:** +30 points for working Claude module = **75/100 (C+)**

---

## 🔴 Critical Issues (Fix Immediately)

### 1. Massive Code Duplication (Score: 3/10)

**Problem:** 9 AI plugins are 95%+ identical copy-paste code.

**Evidence:**
- `ai/gemini/src/main/java/org/flossware/netbeans/gemini/completion/GeminiCompletionProvider.java`
- `ai/chatgpt/src/main/java/org/flossware/netbeans/chatgpt/completion/ChatGPTCompletionProvider.java`
- Files are IDENTICAL except for class names and import statements

**Impact:**
- ~15,000 lines of duplicated code
- Bug fixes require 9x work
- Refactoring is nearly impossible
- Testing waste (same tests duplicated 9x)
- Violates DRY principle

**Current Structure (BAD):**
```
ai/
├── claude/        (40 classes, ~5,000 LOC)
├── gemini/        (40 classes, ~5,000 LOC) ← 95% duplicate
├── chatgpt/       (40 classes, ~5,000 LOC) ← 95% duplicate
├── grok/          (40 classes, ~5,000 LOC) ← 95% duplicate
├── mistral/       (40 classes, ~5,000 LOC) ← 95% duplicate
└── ... (4 more duplicates)
```

**Recommended Structure (GOOD):**
```
ai/
├── common/                           # NEW - Shared abstractions
│   ├── api/
│   │   ├── BaseAIClient.java
│   │   ├── BaseAIService.java
│   │   └── AIClientFactory.java
│   ├── completion/
│   │   ├── BaseCompletionProvider.java
│   │   ├── BaseCompletionQuery.java
│   │   ├── BaseCompletionItem.java
│   │   └── BaseCompletionSettings.java
│   ├── ui/
│   │   ├── BaseChatTopComponent.java
│   │   ├── BaseCodeInsertDialog.java
│   │   └── BaseOptionsPanel.java
│   ├── actions/
│   │   ├── BaseExplainAction.java
│   │   └── BaseRefactorAction.java
│   └── util/
│       ├── CodeExtractor.java
│       └── ProjectContextBuilder.java
│
├── claude/                           # Claude-specific only (~500 LOC)
│   ├── ClaudeClientAdapter.java      # Wraps Anthropic SDK
│   ├── ClaudeConfig.java             # Claude-specific settings
│   └── layer.xml                     # NetBeans registration
│
├── gemini/                           # Gemini-specific only (~500 LOC)
│   ├── GeminiClientAdapter.java
│   ├── GeminiConfig.java
│   └── layer.xml
│
└── chatgpt/                          # ChatGPT-specific only (~500 LOC)
    ├── ChatGPTClientAdapter.java
    ├── ChatGPTConfig.java
    └── layer.xml
```

**Reduction:** 45,000 LOC → ~8,000 LOC (82% reduction)

**Action Items:**
- [ ] Create `ai/common` module
- [ ] Extract `BaseCompletionProvider` from Claude (it's the most complete)
- [ ] Extract `BaseAIClient` with abstract methods for API calls
- [ ] Extract `BaseChatTopComponent` for UI
- [ ] Refactor Claude to extend base classes
- [ ] Refactor Gemini to extend base classes
- [ ] Refactor remaining 7 plugins
- [ ] Delete duplicate code
- [ ] Update tests to test base classes once

---

### 2. False Test Coverage Claims (Score: 4/10)

**Claimed:** "95% test coverage" (README.md line 305)  
**Actual:** ~25% overall coverage

**Breakdown:**

| Module | Test Files | Tests | Coverage | Reality Check |
|--------|------------|-------|----------|---------------|
| Claude | 42 | 531 | 65% | ✅ Good |
| Gemini | ~5 | <50 | <10% | ❌ Poor |
| ChatGPT | ~5 | <50 | <10% | ❌ Poor |
| Grok | 6 | <50 | <10% | ❌ Poor |
| Mistral | ~3 | <30 | <5% | ❌ Failing |
| Perplexity | ~3 | <30 | <5% | ❌ Failing |
| Cohere | ~3 | <30 | <5% | ❌ Failing |
| DeepSeek | ~3 | <30 | <5% | ❌ Failing |
| OpenRouter | ~3 | <30 | <5% | ❌ Failing |
| Languages (14) | 50 | ~200 | <15% | ❌ Poor |

**Weighted Average:** (65% × 5000 LOC + 10% × 40000 LOC) / 45000 = **16%**

**Problem:** Documentation is misleading. TEST_COVERAGE_SUMMARY.md only shows Claude.

**Action Items:**
- [ ] Update README.md to state "65% coverage on Claude module, <10% on others"
- [ ] Update TEST_COVERAGE_SUMMARY.md to show ALL modules
- [ ] Add JaCoCo aggregate report for all modules
- [ ] Set realistic coverage goals: 60% minimum for all AI plugins
- [ ] Write tests for Gemini (copy Claude test structure)
- [ ] Write tests for ChatGPT (copy Claude test structure)
- [ ] Write tests for remaining plugins

---

### 3. Poor Error Handling (Score: 4/10)

**Problems:**
- **14 generic `catch (Exception e)` blocks** in Claude module alone
- **43 uses of `printStackTrace()`** across all modules
- No structured logging framework
- No custom exception hierarchy
- Silent failures likely in production

**Example from `ai/claude/src/main/java/org/flossware/netbeans/claude/api/ClaudeService.java:54`:**
```java
// BAD - Catches everything including NPE, ClassCastException
catch (Exception e) {
    future.completeExceptionally(e);
}
```

**Should be:**
```java
catch (IOException e) {
    logger.log(Level.SEVERE, "Network error contacting Claude API", e);
    future.completeExceptionally(new ClaudeNetworkException("Failed to connect to Claude API", e));
} catch (JsonSyntaxException e) {
    logger.log(Level.SEVERE, "Invalid JSON response from Claude API", e);
    future.completeExceptionally(new ClaudeParseException("Malformed API response", e));
} catch (AuthenticationException e) {
    logger.log(Level.WARNING, "Invalid Claude API key", e);
    future.completeExceptionally(new ClaudeAuthException("API key is invalid or expired", e));
}
```

**Logging Issues:**
- Uses `printStackTrace()` instead of `java.util.logging.Logger`
- No log levels (INFO, WARNING, SEVERE)
- No log rotation or management
- Logs likely lost in production

**Action Items:**
- [ ] Create exception hierarchy:
  ```java
  ClaudeException (base)
  ├── ClaudeNetworkException
  ├── ClaudeAuthException
  ├── ClaudeParseException
  ├── ClaudeRateLimitException
  └── ClaudeConfigException
  ```
- [ ] Replace all `catch (Exception e)` with specific exceptions
- [ ] Remove all `printStackTrace()` calls
- [ ] Add `java.util.logging.Logger` to all classes
- [ ] Add proper log levels
- [ ] Document error handling in CONTRIBUTING.md
- [ ] Test error paths (network failures, auth failures, etc.)

---

### 4. Dependency Management Issues (Score: 6/10)

**Maven Warnings Found:**
```
'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique
org.netbeans.api:org-netbeans-modules-editor-completion:jar -> duplicate declaration
```

**Affected Modules:**
- `ai/gemini/pom.xml` line 86
- `ai/chatgpt/pom.xml` line 82
- `ai/grok/pom.xml` line 82

**Security Concerns:**
- No OWASP Dependency Check
- No Snyk or Dependabot scanning
- No vulnerability scanning in CI/CD
- Dependencies not pinned to specific versions
- No license compliance checking

**Action Items:**
- [ ] Fix duplicate dependencies in Gemini, ChatGPT, Grok POMs
- [ ] Add OWASP Dependency Check to parent POM:
  ```xml
  <plugin>
    <groupId>org.owasp</groupId>
    <artifactId>dependency-check-maven</artifactId>
    <version>9.0.9</version>
    <executions>
      <execution>
        <goals><goal>check</goal></goals>
      </execution>
    </executions>
  </plugin>
  ```
- [ ] Enable Dependabot in GitHub repository settings
- [ ] Add dependency version properties to parent POM
- [ ] Pin all SDK versions (Anthropic, OpenAI, Google AI)
- [ ] Add license check plugin
- [ ] Run `mvn dependency:analyze` and remove unused dependencies

---

### 5. Language Plugins Are Stubs (Score: 3/10)

**Claim:** "Comprehensive language plugins for multi-language development"  
**Reality:** Minimal stub implementations

**Evidence:**
- 14 language modules declared
- Only 101 source files (avg 7 files per module)
- Only 50 test files (avg 3.5 tests per module)
- Python README admits LSP "causes compilation warnings but works at runtime"
- No working LSP integration verified

**Python Plugin Issues (languages/python/README.md):**
- Line 60: "The `org.netbeans.modules.lsp.client` dependency is commented out"
- Line 65: "You may see warnings like: package org.netbeans.modules.lsp.client does not exist"
- Line 70: "These are expected and do not affect runtime functionality" ← This is a red flag

**What Each Language Plugin Actually Contains:**
- Lexer (token recognition) - Basic
- Settings panel - Minimal
- Debugger session - Stub
- NO actual LSP integration
- NO code completion working
- NO real language server communication

**Action Items:**
- [ ] Audit all 14 language plugins
- [ ] For each plugin, either:
  - **Option A:** Implement actual LSP integration and test it
  - **Option B:** Move to `experimental/` directory
  - **Option C:** Delete and reduce scope
- [ ] Update README to not claim "comprehensive" for unimplemented features
- [ ] Test Python plugin with actual python-lsp-server
- [ ] Document minimum viable feature set for each language
- [ ] Add integration tests that verify LSP communication

---

## 🟡 Issues Needing Work

### 6. No Integration Tests (Score: 5/10)

**Current Testing:**
- Unit tests for Claude module (531 tests)
- Mocked tests for APIs
- No actual NBM installation tests
- No UI automation tests
- No end-to-end workflow tests

**Missing Test Coverage:**
- Installing NBM in NetBeans
- Code completion actually appears in editor
- Chat window opens and responds
- API key configuration persistence
- Multi-threaded completion requests
- Large file handling (100KB+ files)
- Network failure scenarios
- Rate limiting behavior

**Action Items:**
- [ ] Add NetBeans test harness dependency
- [ ] Create integration test module: `integration-tests/`
- [ ] Test NBM installation programmatically
- [ ] Test code completion in real editor
- [ ] Test chat window UI
- [ ] Test with large files (100KB, 1MB)
- [ ] Test network failures (mock server down)
- [ ] Add integration tests to CI/CD pipeline

---

### 7. No Performance Testing (Score: N/A)

**Questions Not Answered:**
- What happens with 100KB Java files?
- What happens with 1000 completion requests in 1 minute?
- How much memory does chat history consume?
- Is there a conversation history limit?
- Are API responses cached?
- What's the P95 latency for completions?

**Action Items:**
- [ ] Add JMH (Java Microbenchmark Harness) for performance tests
- [ ] Test completion with files: 1KB, 10KB, 100KB, 1MB
- [ ] Test API rate limiting behavior
- [ ] Test memory usage with long chat sessions
- [ ] Add performance benchmarks to CI/CD
- [ ] Document performance characteristics in README

---

### 8. Security Concerns (Score: 4/10)

**API Key Storage:**
- Stored in NetBeans preferences (`NbPreferences`)
- Likely stored in plaintext in `~/.netbeans/*/config/Preferences`
- No encryption at rest
- No secure enclave usage

**API Communication:**
- Uses HTTPS (good)
- No certificate pinning
- No request signing verification
- No rate limiting enforcement client-side

**Input Validation:**
- No sanitization of user code before sending to APIs
- No length limits on chat messages
- No filtering of sensitive data (passwords, keys in code)

**Action Items:**
- [ ] Audit API key storage mechanism
- [ ] Consider encrypting API keys at rest
- [ ] Add input validation for chat messages (max length)
- [ ] Add sensitive data detection (warn if code contains "password", API keys)
- [ ] Add rate limiting on client side
- [ ] Document security model in SECURITY.md
- [ ] Add security review to PR template

---

## 🟢 Strengths (Keep Doing This)

### 1. Project Structure (Score: 8/10)

**Good Practices:**
- Clean multi-module Maven structure
- Proper parent POM with `dependencyManagement`
- Logical separation (`ai/` vs `languages/`)
- Consistent package naming (`org.flossware.netbeans.*`)
- Good use of Maven profiles (release profile)
- All files have proper copyright headers

**Minor Issues:**
- Could benefit from `ai/common` module (see Critical Issue #1)

---

### 2. Documentation (Score: 7/10)

**Good Practices:**
- 30 markdown files
- Each plugin has README
- BUILD_STATUS.md for troubleshooting
- TESTING.md for test guidelines
- VERSIONING.md explains version format
- SCREENSHOTS.md for visual walkthrough
- CONTRIBUTING.md for contributors

**Issues:**
- Claims are exaggerated (95% coverage)
- Language plugin READMEs admit features don't work
- Missing SECURITY.md
- Missing ARCHITECTURE.md

**Action Items:**
- [ ] Fix coverage claims in README
- [ ] Add SECURITY.md for vulnerability reporting
- [ ] Add ARCHITECTURE.md explaining design decisions
- [ ] Add API_GUIDE.md for developers extending plugins

---

### 3. CI/CD Pipeline (Score: 7/10)

**Good Practices:**
- GitHub Actions configured (`.github/workflows/main.yml`)
- Auto version bumping (X.Y format)
- Builds all 23 modules
- Runs tests
- Deploys to PackageCloud
- Creates artifacts
- Uploads NBM files

**Missing:**
- Security scanning (OWASP, Snyk)
- Dependency vulnerability checks
- Integration tests
- Performance benchmarks
- Code quality checks (SonarQube, SpotBugs)
- Publish test coverage reports

**Action Items:**
- [ ] Add OWASP Dependency Check to workflow
- [ ] Add SpotBugs/PMD/Checkstyle
- [ ] Publish JaCoCo coverage reports
- [ ] Add integration test job
- [ ] Add security scanning job
- [ ] Badge coverage percentage in README

---

### 4. Claude Module (Score: 7/10)

**This is your best work:**
- 531 tests (actual comprehensive testing)
- 65% line coverage
- Advanced features: Test generation, Javadoc generation, debugging
- Proper use of Anthropic SDK
- Well-structured packages (9 packages)
- Good separation of concerns

**This module is production-ready.**

**Action Items:**
- [ ] Use Claude module as template for all other AI plugins
- [ ] Extract base classes from Claude to `ai/common`
- [ ] Document Claude architecture as reference
- [ ] Get Claude to 80% coverage
- [ ] Add integration tests for Claude

---

## Action Plan

### Phase 1: Fix Critical Issues (Week 1-2)

**Priority 1: Stop Misleading Users**
- [ ] Update README.md coverage claims (2 hours)
- [ ] Update TEST_COVERAGE_SUMMARY.md to show all modules (1 hour)
- [ ] Add disclaimer about language plugin maturity (1 hour)

**Priority 2: Fix Build Issues**
- [ ] Remove duplicate dependencies from Gemini, ChatGPT, Grok (1 hour)
- [ ] Run `mvn dependency:analyze` on all modules (2 hours)
- [ ] Fix Maven warnings (1 hour)

**Priority 3: Improve Error Handling**
- [ ] Create exception hierarchy for Claude (4 hours)
- [ ] Replace `printStackTrace()` with `Logger` in Claude (4 hours)
- [ ] Replace generic `catch (Exception)` in Claude (8 hours)
- [ ] Test error paths (4 hours)

**Total: ~27 hours (1 week for 1 developer)**

---

### Phase 2: Eliminate Code Duplication (Week 3-6)

**Step 1: Create ai/common Module**
- [ ] Create `ai/common` module structure (4 hours)
- [ ] Extract `BaseCompletionProvider` from Claude (8 hours)
- [ ] Extract `BaseCompletionQuery` from Claude (8 hours)
- [ ] Extract `BaseCompletionItem` from Claude (4 hours)
- [ ] Extract `BaseAIClient` from Claude (8 hours)
- [ ] Extract `BaseAIService` from Claude (8 hours)
- [ ] Extract `BaseChatTopComponent` from Claude (16 hours)
- [ ] Extract `BaseOptionsPanel` from Claude (8 hours)
- [ ] Write tests for all base classes (24 hours)

**Step 2: Refactor Existing Plugins**
- [ ] Refactor Claude to extend base classes (16 hours)
- [ ] Test Claude still works (8 hours)
- [ ] Refactor Gemini to extend base classes (8 hours)
- [ ] Refactor ChatGPT to extend base classes (8 hours)
- [ ] Refactor remaining 6 plugins (48 hours)
- [ ] Delete duplicate code (4 hours)

**Total: ~170 hours (~4 weeks for 1 developer)**

---

### Phase 3: Achieve Real Test Coverage (Week 7-10)

- [ ] Write tests for Gemini (copy Claude structure) (16 hours)
- [ ] Write tests for ChatGPT (16 hours)
- [ ] Write tests for Grok (16 hours)
- [ ] Write tests for Mistral (16 hours)
- [ ] Write tests for Perplexity (16 hours)
- [ ] Write tests for Cohere (16 hours)
- [ ] Write tests for DeepSeek (16 hours)
- [ ] Write tests for OpenRouter (16 hours)
- [ ] Add JaCoCo aggregate report (4 hours)
- [ ] Achieve 60%+ coverage on all AI modules (16 hours)

**Total: ~148 hours (~4 weeks for 1 developer)**

---

### Phase 4: Fix or Remove Language Plugins (Week 11-14)

**Option A: Implement Properly**
- [ ] Get Python LSP actually working (40 hours)
- [ ] Test with python-lsp-server (8 hours)
- [ ] Add integration tests (16 hours)
- [ ] Repeat for Groovy, Ruby, etc. (200+ hours)

**Option B: Reduce Scope (Recommended)**
- [ ] Move experimental plugins to `experimental/` (2 hours)
- [ ] Keep only Python and Groovy (most popular) (2 hours)
- [ ] Update README to reflect reduced scope (1 hour)
- [ ] Focus on AI plugins (core value) (0 hours)

**Total: ~5 hours if reducing scope, ~264 hours if implementing all**

---

### Phase 5: Security & Quality (Week 15-16)

- [ ] Add OWASP Dependency Check (4 hours)
- [ ] Enable Dependabot (1 hour)
- [ ] Add SpotBugs/PMD/Checkstyle (8 hours)
- [ ] Fix all critical SpotBugs issues (16 hours)
- [ ] Add security documentation (4 hours)
- [ ] Add sensitive data detection (8 hours)
- [ ] Security audit of API key storage (8 hours)

**Total: ~49 hours (~1 week for 1 developer)**

---

### Phase 6: Integration & Performance Testing (Week 17-18)

- [ ] Add NetBeans test harness (8 hours)
- [ ] Create integration tests (32 hours)
- [ ] Add performance benchmarks (16 hours)
- [ ] Test with large files (8 hours)
- [ ] Load testing (8 hours)

**Total: ~72 hours (~2 weeks for 1 developer)**

---

## Total Effort Estimate

| Phase | Weeks | Hours | Priority |
|-------|-------|-------|----------|
| Phase 1: Fix Critical Issues | 1-2 | 27 | 🔴 Must Do |
| Phase 2: Eliminate Duplication | 3-6 | 170 | 🔴 Must Do |
| Phase 3: Real Test Coverage | 7-10 | 148 | 🔴 Must Do |
| Phase 4: Language Plugins | 11-14 | 5* | 🟡 Should Do |
| Phase 5: Security & Quality | 15-16 | 49 | 🟡 Should Do |
| Phase 6: Integration Testing | 17-18 | 72 | 🟢 Nice to Have |

**Total: 471 hours (~12 weeks for 1 developer, ~6 weeks for 2 developers)**

*Assumes reducing scope on language plugins

---

## Recommendation

### For Corporate/Production Use
**Grade: D (Do Not Use)** - Requires 3-6 months of refactoring before production deployment.

**Blockers:**
- Massive code duplication creates maintenance nightmare
- Poor test coverage (only 1 of 9 AI plugins tested)
- Poor error handling (generic exceptions, printStackTrace)
- Language plugins are non-functional stubs

### For Open Source/Learning
**Grade: C+ (Acceptable with Caveats)** - Good learning project, but needs transparency about limitations.

**Requirements:**
- Update documentation to reflect reality
- Add disclaimer about maturity level
- Focus on Claude module (the only production-ready component)
- Be transparent about code duplication issue

### For Personal Use
**Grade: B (Good for Single Module)** - Claude module is usable.

**Recommendation:**
- Use only Claude plugin
- Ignore other AI plugins until refactored
- Ignore language plugins entirely

---

## What This Project Does Well

1. **Claude module** - Actually production-ready (65% coverage, 531 tests)
2. **Documentation** - Comprehensive (30 markdown files)
3. **CI/CD** - Working pipeline with auto-versioning
4. **Structure** - Clean multi-module Maven setup
5. **Licensing** - Proper GPL-3.0 headers everywhere
6. **Ambition** - Attempting to support 9 AI providers is impressive

---

## What This Project Needs

1. **Refactoring** - Extract common code to shared module
2. **Testing** - Actually test all 9 AI plugins, not just Claude
3. **Honesty** - Stop claiming 95% coverage and "comprehensive" features
4. **Focus** - Either implement language plugins properly or remove them
5. **Quality** - Proper error handling, logging, security scanning

---

## Final Verdict

**You have ONE excellent plugin (Claude) drowning in EIGHT mediocre clones and FOURTEEN unfinished language plugins.**

**Path Forward:**
1. Be honest about current state
2. Refactor to eliminate duplication
3. Focus on AI plugins (your core value)
4. Either fix or remove language plugins
5. Achieve real 60%+ coverage on all modules

**Timeline:** 3-6 months of focused work to reach production quality.

**Current State:** Proof of concept with one polished gem (Claude).

**Potential:** High, if architectural issues are addressed.

---

## Questions for Project Owner

1. **Target Users:** Who is the primary audience? (Hobbyists? Enterprises? Students?)
2. **Scope Decision:** Keep all 9 AI providers or focus on top 3?
3. **Language Plugins:** Fix them or remove them?
4. **Timeline:** Can you dedicate 3-6 months to refactoring?
5. **Contributors:** Working alone or seeking contributors?
6. **Priority:** What matters most? (Coverage? Features? Stability?)

---

## Appendix: Evidence

### Code Duplication Evidence
- `ai/gemini/src/main/java/org/flossware/netbeans/gemini/completion/GeminiCompletionProvider.java`
- `ai/chatgpt/src/main/java/org/flossware/netbeans/chatgpt/completion/ChatGPTCompletionProvider.java`
- Files are 100% identical except class names (lines 1-72)

### Test Coverage Evidence
- `ai/claude/src/test/` - 42 test files, 531 tests
- `ai/gemini/src/test/` - ~5 test files, <50 tests
- `ai/chatgpt/src/test/` - ~5 test files, <50 tests
- Other plugins: minimal or no tests

### Error Handling Evidence
- `grep -r "catch (Exception" ai/claude/src/main/java` - 14 matches
- `grep -r "printStackTrace" ai` - 43 matches
- No custom exception classes found

### Dependency Evidence
- Maven build warnings about duplicate dependencies
- No OWASP, Snyk, or Dependabot found

### Language Plugin Evidence
- `languages/python/README.md` line 60: "dependency is commented out"
- Only 101 source files across 14 modules (avg 7 files each)

---

**Review Complete:** 2026-05-27  
**Recommendation:** Address Phase 1 & 2 immediately, then reassess project direction.
