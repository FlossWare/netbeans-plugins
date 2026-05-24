# NetBeans MVEL Support

MVEL (MVFLEX Expression Language) support for Apache NetBeans IDE.

## Overview

This plugin provides basic support for MVEL files in NetBeans, including file recognition, syntax highlighting, and project management. MVEL is an expression language primarily designed to be embedded in Java applications rather than used as a standalone scripting language.

## Important Notes

**MVEL is an Expression Language for Embedded Use**

MVEL is fundamentally different from general-purpose scripting languages like Groovy, Python, or BeanShell. It is designed to be:

- **Embedded** in Java applications as an expression evaluator
- **Used in frameworks** like Drools (rule engine), Spring (configuration), and others
- **Part of template engines** for dynamic content generation
- **Lightweight** for evaluating expressions and simple logic

Unlike languages with standalone interpreters or REPLs, MVEL typically requires integration with a host Java application to execute.

## Features

### Current Features

- **File Recognition**: Automatically recognizes `.mvel` and `.mv` files
- **MIME Type Registration**: Registers `text/x-mvel` MIME type
- **Project Support**: Recognizes projects containing MVEL files
- **Basic Editor Integration**: File handling and basic editor features
- **Settings Management**: Configuration for custom interpreters (if available)

### Limited Features

The following features are provided for consistency with other language plugins but have limitations:

- **LSP Support**: MVEL does not have a widely-adopted Language Server Protocol implementation. The plugin includes LSP infrastructure for future compatibility if an LSP server becomes available.

- **Code Execution**: MVEL does not have a standard standalone interpreter. The "Run MVEL Script" action requires configuring a custom runner or integration with your specific Java application.

- **Interactive Console**: MVEL does not have a standard REPL. The console feature requires a custom interactive interpreter to be configured.

## Installation

### Plugin Installation

1. Build the plugin:
   ```bash
   mvn clean install
   ```

2. Install in NetBeans:
   - Tools > Plugins > Downloaded
   - Click "Add Plugins..."
   - Select the `.nbm` file from `target/`
   - Click "Install"

### MVEL Integration

Since MVEL is typically embedded in applications, you'll need to:

1. **For editing MVEL templates/expressions**: No additional setup needed - the plugin provides file recognition and basic editor support.

2. **For executing MVEL files**: You'll need to create or configure:
   - A custom Java runner that embeds the MVEL runtime
   - Integration with your specific application framework
   - A build script that processes MVEL files

3. **Example Maven dependency** (if using MVEL in your project):
   ```xml
   <dependency>
       <groupId>org.mvel</groupId>
       <artifactId>mvel2</artifactId>
       <version>2.5.0.Final</version>
   </dependency>
   ```

## Usage

### Working with MVEL Files

1. **Create MVEL files**: Files with `.mvel` or `.mv` extensions are automatically recognized
2. **Edit MVEL expressions**: Use the NetBeans editor for syntax highlighting and basic editing
3. **Organize projects**: The plugin recognizes projects containing MVEL files

### Configuring Custom Execution

If you have a custom MVEL interpreter or runner:

1. Go to: Tools > Options > Miscellaneous > MVEL
2. Set the path to your custom interpreter
3. Use the "Run MVEL Script" action from the context menu

### Common Use Cases

#### 1. MVEL in Drools Rules

MVEL is commonly used in Drools rule files:

```mvel
rule "Example Rule"
when
    $person : Person(age > 18)
then
    $person.setAdult(true);
    System.out.println("Person is an adult");
end
```

#### 2. MVEL Templates

MVEL template files for dynamic content:

```mvel
Hello @{person.name},

Your order #@{order.id} has been @{order.status}.
Total: $@{order.total}
```

#### 3. MVEL Expressions

Simple expression files:

```mvel
// Calculate discount
price * 0.9 if customer.vip else price

// Data transformation
{
    'fullName': person.firstName + ' ' + person.lastName,
    'age': person.age,
    'canVote': person.age >= 18
}
```

## File Extensions

The plugin recognizes the following file extensions:

- `.mvel` - MVEL expression/template files
- `.mv` - Alternative MVEL file extension

## Project Recognition

Projects are recognized as MVEL projects if they contain:

- `.mvel` or `.mv` files
- `pom.xml` (Maven project that may use MVEL)
- `build.gradle` (Gradle project that may use MVEL)

## MVEL Language Server Protocol (LSP)

Currently, there is no widely-adopted LSP server for MVEL. The plugin includes LSP infrastructure for:

- Future compatibility if an LSP server is developed
- Consistency with other language plugins in this suite
- Easy integration if you develop a custom LSP server

If a MVEL LSP server becomes available, you can configure it in:
Tools > Options > Miscellaneous > MVEL > LSP Server Path

## Architecture

This plugin follows the common architecture pattern used across all language plugins in this suite:

- **LSP Integration**: `org.flossware.netbeans.mvel.lsp` - Language server integration (future)
- **Completion**: `org.flossware.netbeans.mvel.completion` - Code completion (LSP-based, when available)
- **Execution**: `org.flossware.netbeans.mvel.execution` - Script execution (custom runner required)
- **Project**: `org.flossware.netbeans.mvel.project` - Project type support
- **Settings**: `org.flossware.netbeans.mvel.settings` - Configuration management
- **UI**: `org.flossware.netbeans.mvel.ui` - User interface components

All components extend common base classes from the `netbeans-common` module.

## Development

### Building

```bash
# Build the MVEL plugin
cd languages/mvel
mvn clean install

# Build all plugins
cd ../..
mvn clean install
```

### Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Run specific test
mvn test -Dtest=MvelSettingsTest
```

### Creating a Custom MVEL Runner

If you want to create a custom MVEL interpreter for use with this plugin:

1. Create a Java application that:
   - Accepts MVEL file paths as arguments
   - Uses MVEL API to compile and execute expressions
   - Outputs results to stdout/stderr

2. Example minimal runner:
   ```java
   import org.mvel2.MVEL;
   import java.nio.file.Files;
   import java.nio.file.Paths;
   
   public class MvelRunner {
       public static void main(String[] args) throws Exception {
           String script = new String(Files.readAllBytes(Paths.get(args[0])));
           Object result = MVEL.eval(script);
           System.out.println(result);
       }
   }
   ```

3. Configure the path to your runner in NetBeans settings

## Resources

- **MVEL GitHub**: https://github.com/mvel/mvel
- **MVEL Documentation**: https://github.com/mvel/mvel/wiki
- **Drools (uses MVEL)**: https://www.drools.org/

## Contributing

Contributions are welcome! Please see the main project README for contribution guidelines.

## License

This plugin is part of the FlossWare NetBeans Plugins project and is licensed under the same license as the parent project.

## Support

For issues, questions, or suggestions:

1. Check the main project documentation
2. Review MVEL documentation for language-specific questions
3. Open an issue on GitHub

## Acknowledgments

- The MVEL project team for creating the expression language
- The NetBeans Platform team for the plugin infrastructure
- The FlossWare community for contributions and feedback
