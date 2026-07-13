# AGENTS.md

## Commands

```bash
mvn clean install          # build all modules
mvn test                   # run all tests
mvn test -pl summer-beans -Dtest=DefaultListableBeanFactoryTest   # single test class
mvn test -pl summer-beans -Dtest=DefaultListableBeanFactoryTest#testSomething  # single method
mvn install -DskipTests    # skip tests
```

## Gotchas

### Java 8 bytecode target, tested on JDK 8–21
Bytecode target is Java 8 (runs on JDK 8 through 21). Do not use `var`, records, text blocks, `List.of()`, `Stream.toList()`, switch expressions, or any Java 9+ API.
CI runs a matrix build on JDK 8, 11, 17, and 21 via GitHub Actions.

### Module parent is summer-parent, not root pom
All 4 code modules (`summer-beans`, `summer-validator`, `summer-java-core`, `summer-parent`) inherit from `summer-parent/pom.xml`, not the root `pom.xml`. Dependencies, versions, and plugin versions go in `summer-parent/pom.xml`.

### Stub modules on disk (not in build)
`summer-alarm/` and `summer-batch/` exist on disk but are **not** listed in root `pom.xml` `<modules>`. They have no source code and won't build via `mvn install` from root. Do not treat them as active modules.

### Different test frameworks per module
- **summer-beans**: JUnit **4** (`org.junit.Test`, `org.junit.Assert.*`)
- **summer-java-core**: JUnit **Jupiter 5** (`org.junit.jupiter.api.Test`, AssertJ `assertThat`)
- **summer-validator**: JUnit 4
Do not use JUnit 5 annotations in summer-beans or summer-validator tests.

### Version uses ${revision} property
The project version `0.1.0-SNAPSHOT` is defined as `<revision>` property in root `pom.xml` and referenced as `${revision}` everywhere. When adding new dependency versions, follow the existing `summer-parent` BOM pattern.

### AOP pointcut is hardcoded
`JdkDynamicAopProxy.invoke()` only intercepts method name `"doAction"`. There is no general expression-driven or annotation-driven pointcut mechanism. Do not assume flexible pointcut matching.

### No comments convention
Do not add comments to code unless explicitly asked.

### Auto-generated files
`flatten-maven-plugin` generates `.flattened-pom.xml` in each module during `process-resources`. These are in `.gitignore` and should never be edited.

## Architecture (reference)

See `CLAUDE.md` for full architecture docs. Key module map:

```
summer-parent (BOM, dependencyManagement)
  ├── summer-java-core   (standalone utilities and functional helpers)
  ├── summer-validator    (annotated and fluent validation)
  └── summer-beans        (IoC container, JDK-proxy AOP, DispatcherServlet stub)
```

summer-beans has a `test`-scoped dependency on summer-validator; otherwise, there are no inter-module dependencies among these three.
