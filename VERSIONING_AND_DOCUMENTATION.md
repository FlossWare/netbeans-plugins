# Versioning and Documentation Guide

## Automatic Versioning

This project uses **automatic version bumping** via GitHub Actions, matching the pattern from jcollections.

### Version Format

- **X.Y** format (not X.Y.Z)
- Examples: `1.0`, `1.1`, `1.2`, `2.0`
- No -SNAPSHOT suffix in main branch
- Clean semantic versioning

### How It Works

Every push to `main` triggers:

1. **Parse current version** (e.g., `1.0`)
2. **Increment minor version** (`1.1`)
3. **Update all POMs** (parent + 3 modules automatically)
4. **Build all artifacts**
5. **Deploy to packagecloud.io**
6. **Commit version bump** back to GitHub
7. **Create git tag** (`v1.1`)

### GitHub Actions Workflow

File: `.github/workflows/main.yml`

**Key Steps:**
```yaml
- Incrementing pom.xml version
  mvn build-helper:parse-version versions:set \
      -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion} \
      versions:commit

- Building and Testing
  mvn clean install

- Deploy to packagecloud.io
  mvn deploy

- Checkin and tag to github
  mvn scm:checkin scm:tag
```

### Version Bump Protection

Prevents infinite loop:
```yaml
if: github.event.pusher.email != 'version-bump@flossware.org'
```

The automated commit uses `version-bump@flossware.org` email, so it won't trigger another build.

### Manual Version Commands

**Check current version:**
```bash
mvn help:evaluate -Dexpression=project.version -q -DforceStdout
```

**Set specific version manually:**
```bash
# Set to 2.0
mvn versions:set -DnewVersion=2.0 versions:commit

# Update all modules automatically
git add -A
git commit -m "Manual version bump to 2.0"
git push
```

**Set major version bump:**
```bash
mvn build-helper:parse-version versions:set \
    -DnewVersion=\${parsedVersion.nextMajorVersion}.0 \
    versions:commit
```

### Version History

Track versions via git tags:
```bash
git tag -l
# v1.0
# v1.1
# v1.2
# ...
```

### What Gets Versioned

All modules inherit version from parent:

**Parent POM:**
```xml
<groupId>org.flossware.netbeans</groupId>
<artifactId>netbeans-ai-plugins</artifactId>
<version>1.0</version>  <!-- Single source of truth -->
```

**Module POMs:**
```xml
<parent>
    <groupId>org.flossware.netbeans</groupId>
    <artifactId>netbeans-ai-plugins</artifactId>
    <version>1.0</version>  <!-- Automatically updated with parent -->
</parent>
```

### Generated Artifacts

Each version creates:
```
claude/target/netbeans-claude-integration-1.0.nbm
claude/target/netbeans-claude-integration-1.0-sources.jar
claude/target/netbeans-claude-integration-1.0-javadoc.jar

gemini/target/netbeans-gemini-integration-1.0.nbm
gemini/target/netbeans-gemini-integration-1.0-sources.jar
gemini/target/netbeans-gemini-integration-1.0-javadoc.jar

chatgpt/target/netbeans-chatgpt-integration-1.0.nbm
chatgpt/target/netbeans-chatgpt-integration-1.0-sources.jar
chatgpt/target/netbeans-chatgpt-integration-1.0-javadoc.jar
```

---

## JavaDoc Documentation

### Current JavaDoc Status

✅ **All Java files have JavaDoc comments**
- All 23 classes in Claude module
- All 23 classes in Gemini module
- All 23 classes in ChatGPT module

⚠️ **JavaDoc quality is basic**
- Class-level documentation exists
- Most methods lack detailed documentation
- Parameters and return values not fully documented

### Generating JavaDoc

**Generate for all modules:**
```bash
mvn javadoc:aggregate
# Output: target/site/apidocs/
```

**Generate for specific module:**
```bash
cd claude
mvn javadoc:javadoc
# Output: claude/target/site/apidocs/
```

**Generate and attach JavaDoc JARs:**
```bash
mvn clean package -Prelease
# Creates: *-javadoc.jar files for each module
```

### JavaDoc Plugin Configuration

**Parent POM:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.6.3</version>
    <configuration>
        <show>public</show>
        <nohelp>true</nohelp>
        <encoding>UTF-8</encoding>
    </configuration>
</plugin>
```

### Improving JavaDoc

**Good JavaDoc example:**
```java
/**
 * Provides async service layer for Claude API interactions.
 * 
 * <p>This service manages thread pooling and request queuing for all
 * Claude API calls, ensuring non-blocking operation within NetBeans.</p>
 * 
 * <p><strong>Thread Safety:</strong> This class is thread-safe and uses
 * a singleton pattern. All API calls are executed on a dedicated
 * RequestProcessor thread pool.</p>
 * 
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class ClaudeService {
    
    /**
     * Sends a message to Claude API asynchronously.
     * 
     * @param message the user message to send
     * @return CompletableFuture that completes with Claude's response
     * @throws IllegalArgumentException if message is null or empty
     * @throws IllegalStateException if API key is not configured
     */
    public CompletableFuture<String> sendMessageAsync(String message) {
        // implementation
    }
}
```

### JavaDoc Best Practices

1. **Class-level:**
   - Purpose and responsibility
   - Thread safety notes
   - Usage examples
   - @author, @version, @since tags

2. **Method-level:**
   - What it does (not how)
   - @param for each parameter
   - @return for return value
   - @throws for exceptions
   - Usage examples for complex methods

3. **Field-level:**
   - Document public constants
   - Explain configuration properties

4. **Package-level:**
   - Create package-info.java in each package
   - Describe package purpose and contents

### Viewing JavaDoc

**Local viewing:**
```bash
mvn javadoc:aggregate
open target/site/apidocs/index.html
```

**IDE integration:**
- IntelliJ IDEA: View → Tool Windows → Documentation
- NetBeans: Source → Show Documentation
- VS Code: Hover over class/method names

---

## Markdown Documentation

### Current MD Files

**Parent Level:**
- `README.md` - Multi-module overview
- `BUILD_STATUS.md` - Build troubleshooting
- `PLUGINS_SUMMARY.md` - Feature comparison
- `NETBEANS_PLUGINS_OVERVIEW.md` - Complete project overview
- `RESTRUCTURE_SUMMARY.md` - Multi-module restructure details
- `VERSIONING_AND_DOCUMENTATION.md` - This file

**Module Level:**
- `claude/README.md` - Claude plugin documentation
- `gemini/README.md` - Gemini plugin documentation
- `chatgpt/README.md` - ChatGPT plugin documentation

### Documentation Standards

**README.md should include:**
1. Brief description
2. Features list
3. Installation instructions
4. Configuration steps
5. Usage examples
6. Troubleshooting
7. License

**Keep updated:**
- Version numbers in examples
- Links to external resources
- Feature lists when adding features
- Installation steps if they change

---

## Complete Documentation Build

**Generate everything:**
```bash
# Build artifacts, JavaDoc, and sources
mvn clean package -Prelease

# Generate aggregate JavaDoc site
mvn javadoc:aggregate

# Generates:
# - target/site/apidocs/ (JavaDoc HTML)
# - */target/*-sources.jar (Source code JARs)
# - */target/*-javadoc.jar (JavaDoc JARs)
# - */target/*.nbm (NetBeans plugin files)
```

---

## Distribution

### Where Artifacts Go

**packagecloud.io:**
- Maven repository: `https://packagecloud.io/flossware/java/maven2/`
- NBM files, source JARs, JavaDoc JARs
- Accessible via Maven coordinates

**GitHub Releases:**
```bash
gh release create v1.0 \
  claude/target/netbeans-claude-integration-1.0.nbm \
  gemini/target/netbeans-gemini-integration-1.0.nbm \
  chatgpt/target/netbeans-chatgpt-integration-1.0.nbm \
  --title "NetBeans AI Plugins v1.0" \
  --notes "See CHANGELOG.md"
```

**GitHub Tags:**
- Automatically created: `v1.0`, `v1.1`, etc.
- View: `git tag -l`

---

## Summary

### Versioning ✅
- **Format**: X.Y (e.g., 1.0, 1.1, 2.0)
- **Automation**: Auto-increments on each push to main
- **Consistency**: All modules share same version
- **Tags**: Automatic git tags (v1.0, v1.1, ...)
- **Distribution**: Auto-deploys to packagecloud.io

### JavaDoc ✅
- **Coverage**: All 69 Java classes have JavaDoc
- **Quality**: Basic (needs enhancement)
- **Plugin**: Configured to generate JavaDoc JARs
- **Command**: `mvn javadoc:aggregate`
- **Output**: HTML documentation in target/site/apidocs/

### Markdown ✅
- **Coverage**: README in parent + all 3 modules
- **Guides**: Build, features, restructure, versioning
- **Standards**: Installation, usage, troubleshooting
- **Location**: Parent level (overview), module level (specifics)

---

**Next Steps:**
1. Push to GitHub to trigger first auto-version bump
2. Enhance JavaDoc comments in classes
3. Add package-info.java files for package documentation
4. Create CHANGELOG.md to track version changes
