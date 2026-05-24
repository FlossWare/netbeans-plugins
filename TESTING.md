# Unit Testing Guide

## Overview

This project uses comprehensive unit testing with **JUnit 5**, **Mockito**, **AssertJ**, and **JaCoCo** for code coverage reporting.

## Test Statistics

- **Total Test Files**: 9 (3 per module)
- **Target Coverage**: 60% line coverage (enforced by JaCoCo)
- **Test Framework**: JUnit 5 (Jupiter)
- **Mocking**: Mockito 5.8.0
- **Assertions**: AssertJ 3.24.2

## Test Structure

```
netbeans-plugins/
├── claude/src/test/java/
│   └── org/flossware/netbeans/claude/
│       ├── api/
│       │   ├── ClaudeServiceTest.java
│       │   └── ClaudeClientTest.java
│       └── completion/
│           └── ClaudeCompletionSettingsTest.java
├── gemini/src/test/java/
│   └── org/flossware/netbeans/gemini/
│       ├── api/
│       │   ├── GeminiServiceTest.java
│       │   └── GeminiClientTest.java
│       └── completion/
│           └── GeminiCompletionSettingsTest.java
└── chatgpt/src/test/java/
    └── org/flossware/netbeans/chatgpt/
        ├── api/
        │   ├── ChatGPTServiceTest.java
        │   └── ChatGPTClientTest.java
        └── completion/
            └── ChatGPTCompletionSettingsTest.java
```

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Tests for Specific Module
```bash
# Claude only
mvn test -pl claude

# Gemini only
mvn test -pl gemini

# ChatGPT only
mvn test -pl chatgpt
```

### Run Specific Test Class
```bash
mvn test -Dtest=ClaudeServiceTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=ClaudeServiceTest#testSendMessageAsync_Success
```

### Skip Tests
```bash
mvn clean install -DskipTests
```

## Code Coverage

### Generate Coverage Reports
```bash
mvn clean test jacoco:report
```

### View Coverage Reports
After running tests, coverage reports are generated in:
```
claude/target/site/jacoco/index.html
gemini/target/site/jacoco/index.html
chatgpt/target/site/jacoco/index.html
```

### Coverage Requirements
- **Minimum Line Coverage**: 60%
- **Build Fails**: If coverage is below 60%
- **Enforced By**: JaCoCo Maven Plugin

### Check Coverage Threshold
```bash
mvn clean test jacoco:check
```

## Test Dependencies

Configured in parent POM, inherited by all modules:

```xml
<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.10.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.10.1</version>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.8.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.8.0</version>
    <scope>test</scope>
</dependency>

<!-- AssertJ -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.24.2</version>
    <scope>test</scope>
</dependency>

<!-- MockWebServer (for HTTP API testing) -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>mockwebserver</artifactId>
    <version>4.12.0</version>
    <scope>test</scope>
</dependency>
```

## Test Coverage by Class Type

### Service Classes (High Priority)
**Files**: `ClaudeService`, `GeminiService`, `ChatGPTService`

**Coverage**:
- ✅ Singleton pattern
- ✅ Async message sending
- ✅ Message with context
- ✅ Streaming responses
- ✅ Error handling
- ✅ History management

**Example**:
```java
@Test
void testSendMessageAsync_Success() throws Exception {
    String expectedResponse = "Hello from Claude!";
    when(mockClient.sendMessage(anyString())).thenReturn(expectedResponse);

    CompletableFuture<String> future = service.sendMessageAsync("Hello");
    String result = future.get(5, TimeUnit.SECONDS);

    assertThat(result).isEqualTo(expectedResponse);
    verify(mockClient).sendMessage("Hello");
}
```

### Client Classes (High Priority)
**Files**: `ClaudeClient`, `GeminiClient`, `ChatGPTClient`

**Coverage**:
- ✅ Configuration validation
- ✅ API key requirements
- ✅ History tracking
- ✅ Error messages

**Note**: Full API integration tests require NetBeans runtime and are not included in unit tests.

**Example**:
```java
@Test
void testSendMessage_ThrowsWhenNotConfigured() {
    assertThatThrownBy(() -> client.sendMessage("Hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("API key not configured");
}
```

### Settings Classes (Medium Priority)
**Files**: `*CompletionSettings`

**Coverage**:
- ✅ Default values
- ✅ Getter methods
- ✅ Setter methods (don't throw)

**Example**:
```java
@Test
void testGetMinimumCharacters_DefaultValue() {
    int minChars = ClaudeCompletionSettings.getMinimumCharacters();
    assertThat(minChars).isEqualTo(3);
}
```

## Testing Patterns

### 1. Mocking with Mockito
```java
@ExtendWith(MockitoExtension.class)
class MyTest {
    @Mock
    private ClaudeClient mockClient;
    
    @Test
    void testExample() {
        when(mockClient.sendMessage(anyString())).thenReturn("response");
        // test code
        verify(mockClient).sendMessage("input");
    }
}
```

### 2. Assertions with AssertJ
```java
// String assertions
assertThat(result).isEqualTo("expected");
assertThat(result).contains("substring");

// Boolean assertions
assertThat(condition).isTrue();
assertThat(condition).isFalse();

// Exception assertions
assertThatThrownBy(() -> method())
    .isInstanceOf(IllegalStateException.class)
    .hasMessageContaining("error");

// Null checks
assertThat(object).isNotNull();

// Collection assertions
assertThat(list).hasSize(5);
assertThat(list).contains("item");
```

### 3. Testing CompletableFuture
```java
@Test
void testAsync() throws Exception {
    CompletableFuture<String> future = service.sendMessageAsync("test");
    String result = future.get(5, TimeUnit.SECONDS);
    assertThat(result).isEqualTo("expected");
}
```

### 4. Reflection for Testing Singletons
```java
@BeforeEach
void setUp() {
    // Reset singleton instance
    Field instance = MyClass.class.getDeclaredField("instance");
    instance.setAccessible(true);
    instance.set(null, null);
}
```

## Limitations

### NetBeans Preferences
Many classes depend on NetBeans `NbPreferences` which are not available in standard JUnit tests. These require:
- NetBeans Platform runtime
- Integration test environment
- OR stubbing/mocking the preferences system

**Current Approach**: Test default values and behavior without preferences.

### External API Calls
Real API calls to Claude, Gemini, and ChatGPT are not included in unit tests:
- Would require API keys
- Would be slow
- Would cost money
- Would be unreliable (network issues)

**Current Approach**: Mock all API clients and test business logic only.

### UI Components
Testing TopComponents, OptionsPanel, and other UI classes requires:
- NetBeans Platform runtime
- UI automation framework
- Visual testing tools

**Current Approach**: Not included in unit tests (would be integration tests).

## Future Enhancements

### 1. Integration Tests
```bash
# Separate integration test phase
mvn verify -Pintegration-tests
```

Would test:
- Real NetBeans preferences
- UI components in NetBeans runtime
- Complete plugin lifecycle

### 2. API Contract Tests
Using MockWebServer to test HTTP interactions:
```java
@Test
void testGeminiApiCall() throws Exception {
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse()
        .setBody("{\"response\": \"Hello\"}")
        .setResponseCode(200));
    server.start();
    
    // Test with real HTTP client against mock server
}
```

### 3. Mutation Testing
Using PITest to verify test quality:
```bash
mvn org.pitest:pitest-maven:mutationCoverage
```

### 4. Performance Tests
```java
@Test
void testResponseTime() {
    long start = System.currentTimeMillis();
    service.sendMessage("test");
    long duration = System.currentTimeMillis() - start;
    assertThat(duration).isLessThan(1000);
}
```

## CI/CD Integration

Tests run automatically in GitHub Actions:

```yaml
- name: Building and Testing
  run: "mvn -U clean install"
```

**Coverage Reports**: Could be published to Codecov or similar:
```yaml
- name: Upload coverage to Codecov
  uses: codecov/codecov-action@v3
  with:
    files: ./claude/target/site/jacoco/jacoco.xml,./gemini/target/site/jacoco/jacoco.xml,./chatgpt/target/site/jacoco/jacoco.xml
```

## Test Metrics

### Current Test Count
- **Claude**: 3 test classes, ~25 test methods
- **Gemini**: 3 test classes, ~25 test methods
- **ChatGPT**: 3 test classes, ~25 test methods
- **Total**: 9 test classes, ~75 test methods

### Coverage Goals
- **Service classes**: 80%+ coverage
- **Client classes**: 60%+ coverage
- **Settings classes**: 70%+ coverage
- **Overall**: 60%+ minimum

### Test Execution Time
- **Unit tests**: < 10 seconds total
- **Per module**: < 3 seconds

## Troubleshooting

### Tests fail with "NoClassDefFoundError"
NetBeans dependencies may not be available in test classpath. This is expected for classes that deeply integrate with NetBeans APIs.

**Solution**: Focus tests on business logic, not NetBeans integration.

### Mockito "Cannot mock final class"
Some classes in NetBeans or AI SDKs may be final.

**Solution**: Use Mockito's `mockito-inline` or redesign to inject dependencies.

### Coverage too low
Add tests for:
- Error cases (exceptions)
- Edge cases (null, empty strings)
- All public methods
- Complex business logic

### Tests pass locally but fail in CI
Check:
- Java version (should be 11)
- Maven version
- Environment variables
- Dependencies resolved correctly

## Best Practices

1. **Test behavior, not implementation**
   - Focus on what the code does, not how
   - Avoid testing private methods

2. **One assertion per test** (when possible)
   - Makes failures easier to diagnose
   - Clear test names

3. **Use descriptive test names**
   - Format: `testMethodName_Condition_ExpectedResult`
   - Example: `testSendMessage_EmptyString_ThrowsException`

4. **Mock external dependencies**
   - API clients
   - File systems
   - Network calls

5. **Clean up after tests**
   - Reset singletons
   - Clear static state
   - Close resources

6. **Test edge cases**
   - Null inputs
   - Empty strings
   - Very long strings
   - Special characters

## Resources

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)

---

**Last Updated**: 2026-05-24  
**Test Framework**: JUnit 5.10.1  
**Coverage Tool**: JaCoCo 0.8.11  
**Build Tool**: Maven 3.x
