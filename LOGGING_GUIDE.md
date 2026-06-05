# FlossWare NetBeans Plugins - Logging Guide

## Overview

All FlossWare NetBeans plugins use `java.util.logging` (JUL) for consistent logging across modules. This guide establishes the logging standards and practices for the project.

## Why Proper Logging?

Instead of `System.out.println()`, use proper logging because it:

- **Production Control**: Adjust verbosity without code changes (via logging.properties)
- **Log Rotation**: NetBeans/application handlers can rotate and archive logs
- **Debugging**: Different log levels for development (FINE) vs. production (INFO)
- **Log Aggregation**: Enterprise logging tools can consume structured log streams
- **Performance**: Disabled log levels have near-zero overhead

## Quick Start

### For New Classes

```java
import java.util.logging.Logger;
import java.util.logging.Level;

public class MyService {
    private static final Logger LOG = Logger.getLogger(MyService.class.getName());
    
    public void initializeService() {
        LOG.info("Service initialization started");
        try {
            // ... initialization code ...
            LOG.fine("Detailed initialization step completed");
        } catch (Exception e) {
            LOG.severe("Service initialization failed: " + e.getMessage());
            throw e;
        }
    }
}
```

### Using LoggerFactory (Recommended)

For consistency, use `LoggerFactory` from `ai.core`:

```java
import org.flossware.netbeans.ai.core.logging.LoggerFactory;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MyService {
    private static final Logger LOG = LoggerFactory.getLogger(MyService.class);
    
    public void initializeService() {
        LOG.info("Service initialization started");
        try {
            // ... initialization code ...
            LOG.fine("Detailed initialization step");
        } catch (Exception e) {
            LOG.severe("Service initialization failed: " + e.getMessage());
            throw e;
        }
    }
}
```

## Log Levels

Use the appropriate level for each message:

| Level | Use Case | Production Visible | Example |
|-------|----------|-------------------|---------|
| **SEVERE** | Critical errors, service failures | Yes | "API connection failed: timeout" |
| **WARNING** | Recoverable issues, degraded performance | Yes | "Retry attempt 2/3 for API call" |
| **INFO** | Important state changes, user actions | Yes | "Claude client initialized successfully" |
| **CONFIG** | Configuration details (rarely used) | No | "Logging level set to FINE" |
| **FINE** | Method entry/exit, significant steps | No | "Sending API request to endpoint: /v1/messages" |
| **FINER** | Detailed parameter information | No | "Request parameters: model=claude-sonnet-4-5" |
| **FINEST** | Trace-level debugging | No | "Entering MessageValidator.validateMessage()" |

### Level Selection Rules

- **SEVERE**: Exceptions, unrecoverable errors, service outages
- **WARNING**: Retries, performance degradation, validation failures
- **INFO**: Component initialization, configuration changes, important milestones
- **FINE**: Method-level flow, significant operations, diagnostic details
- **FINER/FINEST**: Development/debugging only, verbose parameter logging

## Logging Patterns

### 1. Method Entry/Exit (FINE level)

```java
LOG.fine("Entering sendMessage()");
try {
    // ... implementation ...
} finally {
    LOG.fine("Exiting sendMessage()");
}
```

### 2. Error Handling

```java
try {
    validateApiKey();
} catch (AuthException e) {
    LOG.severe("Authentication failed: " + e.getMessage());
    throw e;
} catch (Exception e) {
    LOG.warning("Unexpected error during validation: " + e.getClass().getName());
    // ... recovery logic ...
}
```

### 3. Configuration Changes

```java
String apiKey = System.getenv("ANTHROPIC_API_KEY");
if (apiKey == null) {
    LOG.warning("ANTHROPIC_API_KEY environment variable not set");
}
LOG.info("API endpoint configured: " + apiUrl);
```

### 4. Iteration/Loop Progress (FINER level)

```java
for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
    LOG.finer("Retry attempt " + attempt + "/" + MAX_RETRIES);
    try {
        return sendRequest();
    } catch (IOException e) {
        LOG.fine("Request failed, will retry: " + e.getMessage());
    }
}
```

### 5. Parameter Logging (FINEST level)

```java
LOG.finest("Message parameters: " +
    "model=" + model + 
    ", maxTokens=" + maxTokens + 
    ", temperature=" + temperature);
```

## Configuration

### Location

The logging configuration file is located at:
```
ai/core/src/main/resources/logging.properties
```

### Adjusting Log Levels

Edit `logging.properties` to change verbosity:

```properties
# Default level for all FlossWare loggers
org.flossware.netbeans.level=INFO

# Detailed debugging for a specific module
org.flossware.netbeans.claude.api.level=FINE

# Maximum verbosity for core module (development)
org.flossware.netbeans.ai.core.level=FINEST
```

### Production vs. Development

**Production Setup** (logging.properties):
```properties
.level=INFO
java.util.logging.ConsoleHandler.level=INFO
```

**Development Setup** (logging.properties):
```properties
.level=FINE
org.flossware.netbeans.ai.core.level=FINER
java.util.logging.ConsoleHandler.level=FINE
```

## DO's and DON'Ts

### Do

- Use `Logger.info()`, `Logger.warning()`, `Logger.severe()` for user-visible messages
- Use `Logger.fine()`, `Logger.finer()` for diagnostic/development messages
- Include error context: `LOG.severe("Operation failed: " + e.getMessage())`
- Log exceptions with: `LOG.log(Level.SEVERE, "Error occurred", exception)`
- Use appropriate levels to keep production logs clean

### Don't

- **NEVER** use `System.out.println()` or `System.err.println()`
- **NEVER** hardcode log output to files (use handlers in logging configuration)
- **NEVER** log sensitive data (API keys, tokens, passwords)
- **NEVER** log the same information at multiple levels
- **NEVER** use string concatenation in log methods (use parameterized logging if available)
- **NEVER** use log.severe() for expected validation failures (use log.warning() instead)

## Testing

### Unit Tests with Loggers

When testing code that uses logging:

```java
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyServiceTest {
    private MyService service;
    private TestLogHandler logHandler;

    @BeforeEach
    void setUp() {
        service = new MyService();
        logHandler = new TestLogHandler();
        LoggerFactory.getLogger(MyService.class).addHandler(logHandler);
    }

    @Test
    void testServiceInitialization() {
        service.initializeService();
        
        // Verify logging
        assertTrue(logHandler.containsMessage("Service initialization started"));
    }

    // Helper class
    static class TestLogHandler extends Handler {
        private final List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {}

        @Override
        public void close() {}

        boolean containsMessage(String message) {
            return records.stream()
                .anyMatch(r -> r.getMessage().contains(message));
        }
    }
}
```

## Migration from System.out.println()

### Before

```java
System.out.println("Processing file: " + fileName);
System.err.println("Error occurred: " + error);
```

### After

```java
private static final Logger LOG = LoggerFactory.getLogger(MyClass.class);

LOG.info("Processing file: " + fileName);
LOG.severe("Error occurred: " + error);
```

## Troubleshooting

### Logs Not Appearing

1. Check logging level in logging.properties
2. Verify handler configuration
3. Ensure LoggerFactory is being used (not direct `Logger.getLogger()`)
4. Check that class name matches logger configuration

### Too Much Output

Reduce the default level in logging.properties:
```properties
.level=WARNING  # Only SEVERE and WARNING
```

### Can't Find Logs

Logs go to:
- **Development**: NetBeans console (Tools > Output)
- **Production**: Configured handler location (typically logs/ directory)

## References

- [Java Logging Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/logging/package-summary.html)
- [Logging Best Practices](https://docs.oracle.com/javase/tutorial/i18n/logging/)
- [NetBeans Logging](https://netbeans.apache.org/tutorials/nbm-filetemplates.html)
