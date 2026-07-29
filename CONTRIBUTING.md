# Contributing to Summer

Thanks for your interest in contributing to Summer.

## Getting Started

1. Fork the repository on GitHub
2. Clone your fork locally
3. Run the build to make sure everything works:

```bash
mvn clean install
mvn test
```

## Development

- Java 8 bytecode target, tested on JDK 8 through 21
- Do not use Java 9+ APIs (no `var`, records, text blocks, `List.of()`, etc.)
- Follow existing code style and conventions in each module
- Module structure is documented in `CLAUDE.md`

### Build Commands

```bash
mvn test -pl summer-beans -Dtest=DefaultListableBeanFactoryTest   # single test
mvn install -DskipTests                                           # skip tests
```

### Code Style

- Do not add comments unless necessary
- Keep changes minimal and focused
- Write tests for new functionality

## Pull Request Process

1. Create a branch for your changes
2. Make your changes and run the full test suite
3. Submit a pull request with a clear description of the changes

## Questions

Open an issue on GitHub or contact the maintainer at congccoder@gmail.com.
