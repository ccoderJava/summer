# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build the entire project (Java 8+ required)
mvn clean install

# Run all tests across all modules
mvn test

# Run tests in a specific module
mvn test -pl summer-beans
mvn test -pl summer-validator
mvn test -pl summer-java-core

# Run a single test class
mvn test -pl summer-beans -Dtest=DefaultListableBeanFactoryTest
mvn test -pl summer-java-core -Dtest=PredicatesTest

# Skip tests during build
mvn install -DskipTests
```

CI runs `mvn -B test` (CircleCI) and `mvn -B package` (GitHub Actions) on push/PR to `main`. Java 8 is the target version.

## Project Architecture

Summer is a **minimal IoC/AOP kernel for Java 8**, inspired by the Spring Framework. It's a Maven multi-module project (`groupId: io.github.dianpoint`, version `0.1.0-SNAPSHOT`) with four modules:

### Module Dependency Tree

```
summer-parent (BOM — dependencyManagement for all versions)
  ├── summer-java-core  (standalone utilities, no internal deps)
  ├── summer-validator   (standalone validation, no internal deps)
  └── summer-beans       (IoC/AOP/MVC — test-scoped dep on summer-validator)
```

### summer-beans — IoC Container, AOP, Web

The core module. Package layout:

- **`beans.factory`** — BeanFactory interfaces (`BeanFactory`, `ListableBeanFactory`, `AutowireCapableBeanFactory`, `ConfigurableBeanFactory`) and their implementations. The concrete factory is `DefaultListableBeanFactory`.
- **`beans.factory.config`** — `BeanDefinition`, `BeanPostProcessor`, `BeanFactoryPostProcessor`, constructor arguments, property values.
- **`beans.factory.support`** — `DefaultListableBeanFactory`, `DefaultSingletonBeanRegistry`, `BeanDefinitionRegistry`.
- **`beans.factory.xml`** — `XmlBeanDefinitionReader` parses XML via dom4j, producing `BeanDefinition` objects.
- **`beans.factory.annotation`** — `@Autowired` and `AutowiredAnnotationBeanPostProcessor` (field injection by name).
- **`context`** — `ApplicationContext` hierarchy. `AbstractApplicationContext` defines the `refresh()` template; `ClassPathXmlApplicationContext` is the concrete implementation.
- **`aop`** — JDK dynamic proxy-based AOP. See AOP section below.
- **`web`** — `DispatcherServlet` (minimal stub extending `HttpServlet`) and `@RequestMapping`.
- **`core`** — `Resource` abstraction for loading classpath XML files.
- **`core.env`** — `Environment` / `PropertyResolver` interfaces.

#### BeanFactory Class Hierarchy

```
DefaultSingletonBeanRegistry  (singleton storage, singleton/earlySingleton maps)
  └── AbstractBeanFactory     (bean creation, property injection, init-method invocation)
        └── AbstractAutowireCapableBeanFactory  (BeanPostProcessor registration + lifecycle)
              └── DefaultListableBeanFactory    (concrete, query-by-type, getBeansOfType)
```

#### Bean Lifecycle

1. XML parsed by `XmlBeanDefinitionReader` → `BeanDefinition` objects registered in `BeanDefinitionRegistry`
2. `AbstractBeanFactory.getBean(name)` checks singleton cache → `earlySingletonObjects` → calls `createBean(beanDefinition)`
3. `createBean`: instantiate via `doCreateBean` (reflection, constructor injection or no-arg) → stash in `earlySingletonObjects` → `handleProperties` (setter injection, including `ref` dependencies resolved recursively via `getBean`) → register as singleton
4. `BeanPostProcessor.postProcessBeforeInitialization` runs
5. `init-method` (if defined) invoked via reflection
6. `BeanPostProcessor.postProcessAfterInitialization` would run (currently stubbed)

#### ApplicationContext Refresh Lifecycle

`AbstractApplicationContext.refresh()` orchestrates:
1. `postProcessBeanFactory()` — template method
2. `registerBeanPostProcessors()` — registers `AutowiredAnnotationBeanPostProcessor`
3. `initApplicationEventPublisher()` — creates `SimpleApplicationEventPublisher`
4. `onRefresh()` — calls `beanFactory.refresh()` which eagerly instantiates all non-lazy singleton beans
5. `registerListeners()` — registers an `ApplicationListener`
6. `finishRefresh()` — publishes `ContextRefreshEvent`

#### AOP Architecture

JDK dynamic proxy only (no CGLIB). The entry point is `ProxyFactoryBean` (implements `FactoryBean<Object>`).

```
ProxyFactoryBean
  └── AopProxyFactory (DefaultAopProxyFactory)
        └── AopProxy (JdkDynamicAopProxy implements InvocationHandler)
              └── Advisor (DefaultAdvisor)
                    └── MethodInterceptor
```

**Advice type hierarchy:**
- `Advice` (root marker) → `Interceptor` (marker) → `MethodInterceptor` (core: `invoke(MethodInvocation)`)
- `BeforeAdvice` → `MethodBeforeAdvice` (defines `before(method, args, target)`)
- `AfterAdvice` → `AfterReturningAdvice` (defines `afterReturning(returnValue, method, args, target)`)

**Adapter pattern** — typed advice is wrapped into `MethodInterceptor`:
- `MethodBeforeAdviceInterceptor` wraps `MethodBeforeAdvice` (calls `before()` then `proceed()`)
- `AfterReturningAdviceInterceptor` wraps `AfterReturningAdvice` (calls `proceed()` then `afterReturning()`)

**Invocation chain**: `JdkDynamicAopProxy.invoke()` matches method name `"doAction"` (hardcoded pointcut), creates a `ReflectiveMethodInvocation`, and delegates to the advisor's `MethodInterceptor`, which calls `invocation.proceed()` at the appropriate point. Single-interceptor only — no multi-interceptor chain.

### summer-validator — Validation Framework

Standalone module with no internal dependencies.

Two validation approaches:

1. **Fluent/functional API**: `Validators.generic()`, `Validators.string()`, `Validators.integer()` return pre-configured `GenericValidator<T>` instances. `Validators.collection()` returns `DefaultCollectionValidator`. Rules are `Predicate<T>` lambdas.

2. **Annotation-based API**: `Validators.annotated(User.class)` returns an `AnnotationValidatorAdapter` that scans for `@NotNull` and `@Pattern` annotations and runs corresponding `AnnotationProcessor` implementations registered in `AnnotationProcessorRegister`.

### summer-java-core — Utilities

- `Wrapper` — interface for unwrapping/delegating objects
- `Streams`, `Predicates` — functional helpers
- `AssertUtils` — assertion utilities
- `ThrowableAction`, `ThrowableConsumer`, `ThrowableFunction`, `ThrowableSupplier` — functional interfaces that allow checked exceptions (for use with lambdas)
- `SetUtils` — set utilities

### summer-parent — BOM

Centralizes all dependency versions (Jackson, dom4j, servlet-api, Lombok, JUnit 4+5, AssertJ). Child modules inherit from this, not the root POM.

## Key Dependencies

- **dom4j 2.1.4** — XML parsing for bean definitions
- **Jackson 2.18.3** — JSON support
- **Servlet API 2.5** — `provided` scope, for `DispatcherServlet`
- **Lombok 1.18.12** — used in summer-java-core
- **JUnit 4.13.2** — summer-beans tests
- **JUnit Jupiter 5.12.2** + **AssertJ 3.24.2** — summer-java-core tests

## Testing

- **summer-beans**: Uses JUnit 4 (`@Test` + `assert*`). Tests for bean factory, XML loading, autowiring, circular dependency resolution, AOP proxies, and the application context.
- **summer-java-core**: Uses JUnit Jupiter 5 + AssertJ. Tests for `Predicates` and `AssertUtils`.
- **summer-validator**: Has a `ValidatorDemo` test class and a `User` test model using `@NotNull`/`@Pattern` annotations.
