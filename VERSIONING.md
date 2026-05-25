# Versioning and Release Process

## Version Format

This project uses **X.Y** semantic versioning:
- **X**: Major version (breaking changes)
- **Y**: Minor version (features, fixes, incremented automatically)

**Example versions**: `1.0`, `1.1`, `1.2`, `2.0`

## Auto-Versioning System

### How It Works

Every push to `main` triggers automatic version increment via GitHub Actions:

1. **Parse Current Version**
   ```bash
   mvn build-helper:parse-version versions:set \
     -DnewVersion=${parsedVersion.majorVersion}.${parsedVersion.nextMinorVersion}
   ```

2. **Build and Test**
   ```bash
   mvn clean install
   ```

3. **Deploy to PackageCloud**
   ```bash
   mvn -DskipTests deploy
   ```

4. **Commit Version Bump**
   ```bash
   mvn scm:checkin scm:tag
   ```

### Version Increment Examples

| Current | After Auto-Bump | Description |
|---------|----------------|-------------|
| `1.0` | `1.1` | Minor increment |
| `1.1` | `1.2` | Minor increment |
| `1.9` | `1.10` | Minor increment |
| `1.99` | `1.100` | Minor increment |

### Avoiding Infinite Loops

The workflow only runs if the pusher email is **NOT** `version-bump@flossware.org`:

```yaml
if: github.event.pusher.email != 'version-bump@flossware.org'
```

This prevents the version bump commit from triggering another build.

## Manual Version Changes

### Incrementing Minor Version

```bash
# Auto-increment minor version (1.0 → 1.1)
mvn build-helper:parse-version versions:set \
  -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion} \
  versions:commit
```

### Incrementing Major Version

```bash
# Manual major version bump (1.9 → 2.0)
mvn versions:set -DnewVersion=2.0 versions:commit
```

### Setting Specific Version

```bash
# Set exact version
mvn versions:set -DnewVersion=1.5 versions:commit
```

## Version Validation

Maven Enforcer Plugin validates version format on every build:

```xml
<evaluateBeanshell>
    <condition>
        String v = "${project.version}";
        v.matches("^\\d+\\.\\d+$")
    </condition>
    <message>VERSION ERROR: Version must be X.Y format and NOT a SNAPSHOT.</message>
</evaluateBeanshell>
```

### Valid Versions
- ✅ `1.0`
- ✅ `1.1`
- ✅ `2.0`
- ✅ `1.100`

### Invalid Versions
- ❌ `1.0.0` (three components)
- ❌ `1.0-SNAPSHOT` (SNAPSHOT suffix)
- ❌ `v1.0` (prefix)
- ❌ `1` (missing minor version)

## Multi-Module Versioning

The parent POM defines the version:

```xml
<groupId>org.flossware.netbeans</groupId>
<artifactId>plugins</artifactId>
<version>1.0</version>
```

All child modules inherit this version:

```xml
<parent>
    <groupId>org.flossware.netbeans</groupId>
    <artifactId>plugins</artifactId>
    <version>1.0</version>
    <relativePath>../../pom.xml</relativePath>
</parent>
```

**Result**: Single version command updates all 18 modules simultaneously.

## Release Process

### Automatic Release (Recommended)

1. Push changes to `main`:
   ```bash
   git push origin main
   ```

2. GitHub Actions automatically:
   - Increments version (e.g., `1.0` → `1.1`)
   - Builds all 18 NBM files
   - Runs 454 tests
   - Verifies 95% coverage
   - Deploys to PackageCloud
   - Commits version bump
   - Creates git tag `1.1`

3. Download NBM files from GitHub Actions artifacts or PackageCloud

### Manual Release

For major version changes or special releases:

1. **Update Version**
   ```bash
   mvn versions:set -DnewVersion=2.0 versions:commit
   ```

2. **Build and Test**
   ```bash
   mvn clean install
   ```

3. **Verify NBM Files**
   ```bash
   find . -name "*.nbm" -path "*/target/nbm/*"
   # Should show 18 NBM files
   ```

4. **Deploy**
   ```bash
   mvn -DskipTests deploy
   ```

5. **Commit and Tag**
   ```bash
   git add .
   git commit -m "Release version 2.0"
   mvn scm:tag
   git push origin main
   git push origin 2.0
   ```

## Git Tags

Every version is tagged in git:

```bash
# List all version tags
git tag

# Checkout specific version
git checkout 1.5

# Show tag details
git show 1.5
```

## PackageCloud Repository

All versions are deployed to: https://packagecloud.io/flossware/java/maven2/

### Download Specific Version

```xml
<dependency>
    <groupId>org.flossware.netbeans</groupId>
    <artifactId>netbeans-claude-integration</artifactId>
    <version>1.5</version>
    <type>nbm</type>
</dependency>
```

## Version History

Track version history via:

1. **Git Tags**
   ```bash
   git tag -l
   ```

2. **GitHub Releases**
   https://github.com/FlossWare/netbeans-plugins/releases

3. **PackageCloud**
   https://packagecloud.io/flossware/java

## CI/CD Workflow

### Complete Build Pipeline

```
Push to main
    ↓
Checkout code
    ↓
Increment version (1.0 → 1.1)
    ↓
Build all 18 modules
    ↓
Run 454 tests
    ↓
Verify 95% coverage
    ↓
Create 18 NBM files
    ↓
Deploy to PackageCloud
    ↓
Commit version bump
    ↓
Create git tag (1.1)
    ↓
Push to GitHub
```

### Workflow File

Location: `.github/workflows/main.yml`

Key sections:
- Version increment: `mvn build-helper:parse-version versions:set`
- Git credentials: `version-bump@flossware.org`
- Commit message: `Automated Version Bump 1.1 [ci skip]`

## Troubleshooting

### Build Fails After Version Bump

**Problem**: Build fails immediately after auto-version bump.

**Solution**: Check that all child POMs use `${project.version}` or inherit from parent.

### Version Not Incrementing

**Problem**: Pushed to main but version didn't increment.

**Solution**: Check if commit author is `version-bump@flossware.org` (would be skipped).

### Maven Enforcer Failure

**Problem**: Build fails with "VERSION ERROR: Version must be X.Y format"

**Solutions**:
1. Remove `-SNAPSHOT` suffix
2. Remove third component (use `1.0` not `1.0.0`)
3. Use only digits and dot (no `v` prefix)

### Infinite Build Loop

**Problem**: Every commit triggers another build infinitely.

**Solution**: Verify workflow has email check:
```yaml
if: github.event.pusher.email != 'version-bump@flossware.org'
```

## Best Practices

### Development Workflow

1. ✅ Work on feature branches
2. ✅ Create PR to `main`
3. ✅ Let CI auto-increment version
4. ✅ Tag major releases manually

### DO

- ✅ Use X.Y format only
- ✅ Let CI handle minor increments
- ✅ Manually bump major version for breaking changes
- ✅ Tag significant releases
- ✅ Document breaking changes

### DON'T

- ❌ Use `1.0.0` format
- ❌ Add `-SNAPSHOT` suffix
- ❌ Manually edit version in child POMs
- ❌ Push with email `version-bump@flossware.org`
- ❌ Skip version validation

## Related Documentation

- **Maven Versions Plugin**: https://www.mojohaus.org/versions-maven-plugin/
- **Build Helper Plugin**: https://www.mojohaus.org/build-helper-maven-plugin/
- **Maven SCM Plugin**: https://maven.apache.org/scm/maven-scm-plugin/
- **Maven Enforcer Plugin**: https://maven.apache.org/enforcer/maven-enforcer-plugin/

## Version Commands Reference

```bash
# Show current version
mvn help:evaluate -Dexpression=project.version -q -DforceStdout

# Increment minor version (auto)
mvn build-helper:parse-version versions:set \
  -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion} \
  versions:commit

# Set specific version
mvn versions:set -DnewVersion=2.0 versions:commit

# Revert version change (before commit)
mvn versions:revert

# Update child module versions to match parent
mvn versions:update-child-modules

# Display dependency updates
mvn versions:display-dependency-updates

# Display plugin updates
mvn versions:display-plugin-updates
```

## Summary

**Current Version**: `1.0`  
**Format**: X.Y (no SNAPSHOT)  
**Auto-Increment**: Every push to main  
**Deployment**: PackageCloud + GitHub Releases  
**Validation**: Maven Enforcer Plugin  
**Documentation**: This file + commit messages  

---

**Last Updated**: 2024-05-25  
**Pattern Source**: Based on [jcollections](https://github.com/FlossWare/jcollections) versioning
