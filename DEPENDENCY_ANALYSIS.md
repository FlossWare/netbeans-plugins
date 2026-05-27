# Dependency Analysis & Cleanup

**Last Updated:** 2026-05-27  
**Status:** Phase 1 fixes applied

---

## Issues Fixed

### 1. Duplicate Dependencies (FIXED)

**Problem:** Maven warnings about duplicate dependency declarations

**Affected Modules:**
- ✅ `ai/gemini/pom.xml` - Fixed (removed duplicate editor-completion)
- ✅ `ai/chatgpt/pom.xml` - Fixed (removed duplicate editor-completion)
- ✅ `ai/grok/pom.xml` - Fixed (removed duplicate editor-completion)

**Impact:** Build now succeeds without warnings

---

## Claude Module Dependency Review

### Used Undeclared Dependencies (Need to Add)

These are used in code but not explicitly declared:

```xml
<!-- Should add to pom.xml -->
<dependency>
    <groupId>org.netbeans.api</groupId>
    <artifactId>org-netbeans-api-progress</artifactId>
</dependency>
<dependency>
    <groupId>org.netbeans.api</groupId>
    <artifactId>org-netbeans-api-progress-nb</artifactId>
</dependency>
```

**Note:** These work currently via transitive dependencies, but should be explicit.

### Unused Declared Dependencies (Consider Removing)

These are declared but not used in code:

```xml
<!-- Not directly used - could remove if confirmed -->
org.netbeans.api:org-netbeans-api-annotations-common
org.netbeans.api:org-netbeans-modules-editor (unused)
org.netbeans.api:org-netbeans-modules-projectuiapi (unused)
org.netbeans.api:org-netbeans-modules-editor-settings (unused)
org.netbeans.api:org-netbeans-modules-lexer (unused)
org.junit.jupiter:junit-jupiter-engine (test scope issue)
```

**Recommendation:** Keep for now (may be used by annotations/reflection), remove in Phase 2

### Test-Only Dependencies in Compile Scope

```xml
<!-- Should be test scope only -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <scope>test</scope> <!-- Add this -->
</dependency>
```

---

## Security Concerns (To Address)

### Missing Security Scanning

**Currently Missing:**
- ❌ OWASP Dependency Check
- ❌ Snyk vulnerability scanning
- ❌ Dependabot alerts
- ❌ License compliance checking

### Recommendation: Add to Parent POM

```xml
<build>
    <plugins>
        <!-- OWASP Dependency Check -->
        <plugin>
            <groupId>org.owasp</groupId>
            <artifactId>dependency-check-maven</artifactId>
            <version>9.0.9</version>
            <executions>
                <execution>
                    <goals>
                        <goal>check</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <failBuildOnCVSS>7</failBuildOnCVSS>
            </configuration>
        </plugin>

        <!-- Versions Maven Plugin -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>versions-maven-plugin</artifactId>
            <version>2.16.2</version>
        </plugin>
    </plugins>
</build>
```

### Enable Dependabot

Create `.github/dependabot.yml`:

```yaml
version: 2
updates:
  - package-ecosystem: "maven"
    directory: "/"
    schedule:
      interval: "weekly"
    open-pull-requests-limit: 10
```

---

## Dependency Version Pinning

### Current State

**Good:**
- ✅ Anthropic SDK: `2.32.0` (pinned)
- ✅ Google AI: `${google-ai.version}` (centralized)
- ✅ OpenAI SDK: `${openai.version}` (centralized)

**Needs Improvement:**
- ⚠️ Gson: `2.10.1` (should be in parent POM)
- ⚠️ OkHttp: `4.12.0` (should be in parent POM)
- ⚠️ AssertJ: `3.25.1` (should be in parent POM)

### Recommendation: Centralize in Parent POM

```xml
<properties>
    <!-- Test frameworks -->
    <junit.version>5.10.1</junit.version>
    <mockito.version>5.8.0</mockito.version>
    <assertj.version>3.25.1</assertj.version>
    
    <!-- HTTP clients -->
    <okhttp.version>4.12.0</okhttp.version>
    
    <!-- JSON -->
    <gson.version>2.10.1</gson.version>
    
    <!-- AI SDKs -->
    <anthropic.version>2.32.0</anthropic.version>
    <openai.version>0.18.2</openai.version>
    <google-ai.version>0.32.0</google-ai.version>
</properties>
```

---

## Known Vulnerabilities (To Check)

### Run OWASP Check

```bash
mvn org.owasp:dependency-check-maven:check
```

**Expected Issues:**
- Older dependencies may have known CVEs
- OkHttp versions should be checked
- SDK versions should be latest stable

### Update Strategy

1. **Check for updates:**
   ```bash
   mvn versions:display-dependency-updates
   ```

2. **Update dependencies:**
   ```bash
   mvn versions:use-latest-releases
   ```

3. **Test thoroughly:**
   ```bash
   mvn clean test
   ```

---

## Transitive Dependency Tree

### Claude Module (Simplified)

```
netbeans-claude-integration
├── com.anthropic:anthropic-java:2.32.0
│   ├── anthropic-java-client-okhttp:2.32.0
│   └── anthropic-java-core:2.32.0
├── com.google.code.gson:gson:2.10.1
├── com.squareup.okhttp3:okhttp:4.12.0
└── org.netbeans.api:* (22 NetBeans Platform deps)
    └── org.openide.* (various)
```

### Gemini Module (Simplified)

```
netbeans-gemini-integration
├── com.google.cloud:google-cloud-aiplatform:0.32.0
│   └── Many Google Cloud dependencies
├── com.google.code.gson:gson:2.10.1
├── com.squareup.okhttp3:okhttp:4.12.0
└── org.netbeans.api:* (22 NetBeans Platform deps)
```

---

## Action Items

### Completed ✅
- [x] Remove duplicate dependencies (Gemini, ChatGPT, Grok)
- [x] Verify build succeeds
- [x] Document dependency issues

### Phase 2 (Recommended)
- [ ] Add explicit progress API dependencies to Claude
- [ ] Move OkHttp to test scope where appropriate
- [ ] Centralize version properties in parent POM
- [ ] Add OWASP Dependency Check plugin
- [ ] Enable Dependabot
- [ ] Run vulnerability scan
- [ ] Update outdated dependencies
- [ ] Add license compliance check

### Phase 3 (Nice to Have)
- [ ] Analyze all 23 modules
- [ ] Remove truly unused dependencies
- [ ] Optimize dependency tree
- [ ] Document required vs optional dependencies

---

## How to Verify

### 1. Check for Duplicates

```bash
mvn validate | grep "duplicate"
```

**Expected:** No warnings (after fixes)

### 2. Analyze Dependencies

```bash
cd ai/claude
mvn dependency:analyze
```

**Expected:** Warnings about used-undeclared and unused-declared (documented above)

### 3. Check for Updates

```bash
mvn versions:display-dependency-updates
```

### 4. Build All Modules

```bash
mvn clean compile -DskipTests
```

**Expected:** BUILD SUCCESS

---

## Best Practices Going Forward

### For New Dependencies

1. **Add to parent POM first:**
   ```xml
   <dependencyManagement>
       <dependencies>
           <dependency>
               <groupId>...</groupId>
               <artifactId>...</artifactId>
               <version>${version.property}</version>
           </dependency>
       </dependencies>
   </dependencyManagement>
   ```

2. **Reference without version in modules:**
   ```xml
   <dependency>
       <groupId>...</groupId>
       <artifactId>...</artifactId>
       <!-- version managed by parent -->
   </dependency>
   ```

3. **Use properties for versions:**
   ```xml
   <properties>
       <mylib.version>1.2.3</mylib.version>
   </properties>
   ```

### Regular Maintenance

1. **Weekly:** Check Dependabot PRs
2. **Monthly:** Run `mvn versions:display-dependency-updates`
3. **Quarterly:** Run OWASP check
4. **Before Release:** Full vulnerability scan

---

## Related Documents

- **CRITICAL_ASSESSMENT.md** - Section "Dependency Management Issues"
- **Parent POM** - `/pom.xml` (centralized dependency management)
- **Individual POMs** - `ai/*/pom.xml`, `languages/*/pom.xml`

---

**Maintained By:** FlossWare Project  
**Last Review:** 2026-05-27  
**Next Review:** Phase 2 (after code duplication fix)
