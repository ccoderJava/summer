# Summer Framework 优化路线图

> 基于代码深度分析的增量开发计划，按 Tier 依次执行。

## 已完成 (Tier 0 – 2)

| PR | 内容 | 链接 |
|---|---|---|
| #32 | Prototype scope 支持 | [PR #32](https://github.com/dianpoint/summer/pull/32) |
| #33 | @PostConstruct / @PreDestroy 生命周期 | [PR #33](https://github.com/dianpoint/summer/pull/33) |
| #34 | BeanFactoryPostProcessor 调用 | [PR #34](https://github.com/dianpoint/summer/pull/34) |
| #35 | @Value 属性注入 + 占位符解析 | [PR #35](https://github.com/dianpoint/summer/pull/35) |
| #36 | ApplicationListener 接口重构 | [PR #36](https://github.com/dianpoint/summer/pull/36) |
| #37 | @Component + ComponentScan | [PR #37](https://github.com/dianpoint/summer/pull/37) |
| #38 | @Configuration + @Bean 注解配置 | [PR #38](https://github.com/dianpoint/summer/pull/38) |
| #39 | @Qualifier 限定注入 | [PR #39](https://github.com/dianpoint/summer/pull/39) |

---

## Tier 3 — AOP 完善

> 当前 AOP 拦截所有方法、仅支持单个拦截器，需完善切面表达能力。

### 3.1 Pointcut 方法名匹配

**现状**：`JdkDynamicAopProxy.invoke()` 无条件拦截所有方法调用。

**任务**：
- [ ] 创建 `Pointcut` 接口，定义 `boolean matches(Method method, Class<?> targetClass)`
- [ ] 实现 `NameMatchMethodPointcut`，支持通配符 `*` 匹配（如 `doAction`、`*Service`、`do*`）
- [ ] 修改 `JdkDynamicAopProxy.invoke()` 加入 Pointcut 过滤逻辑
- [ ] 修改 `Advisor` 接口添加 `getPointcut()` / `setPointcut()`
- [ ] 修改 `DefaultAdvisor` 实现 Pointcut 支持

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `aop/Pointcut.java` |
| 新增 | `aop/NameMatchMethodPointcut.java` |
| 修改 | `aop/Advisor.java` |
| 修改 | `aop/DefaultAdvisor.java` |
| 修改 | `aop/JdkDynamicAopProxy.java` |
| 新增 | `test/aop/NameMatchMethodPointcutTest.java` |

### 3.2 多拦截器链

**现状**：`DefaultAdvisor` 仅持有单个 `MethodInterceptor`。

**任务**：
- [ ] 修改 `Advisor` 接口，`getMethodInterceptors()` 返回 `List<MethodInterceptor>`
- [ ] 修改 `DefaultAdvisor` 支持多拦截器列表
- [ ] `ReflectiveMethodInvocation` 改为链式执行：维护当前拦截器索引，`proceed()` 递归调用下一个
- [ ] 修改 `ProxyFactoryBean.initializeAdvisor()` 支持多 Advice

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 修改 | `aop/Advisor.java` |
| 修改 | `aop/DefaultAdvisor.java` |
| 修改 | `aop/ReflectiveMethodInvocation.java` |
| 修改 | `aop/ProxyFactoryBean.java` |
| 修改 | `aop/JdkDynamicAopProxy.java` |
| 新增 | `test/aop/InterceptorChainTest.java` |

### 3.3 @Around + @AfterThrowing 通知

**现状**：仅有 `BeforeAdvice` 和 `AfterReturningAdvice`。

**任务**：
- [ ] 创建 `ThrowsAdvice` 接口（`afterThrowing` 方法）
- [ ] 创建 `AfterThrowingAdviceInterceptor` 适配器
- [ ] 创建 `AroundAdvice` 接口（`around` 方法，接受 `ProceedingJoinPoint`）
- [ ] 创建 `AroundAdviceInterceptor` 适配器
- [ ] `ProxyFactoryBean` 增加 `AroundAdvice` / `ThrowsAdvice` 分支识别
- [ ] 创建 `ProceedingJoinPoint`（包装 `MethodInvocation`）

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `aop/ThrowsAdvice.java` |
| 新增 | `aop/AfterThrowingAdviceInterceptor.java` |
| 新增 | `aop/AroundAdvice.java` |
| 新增 | `aop/AroundAdviceInterceptor.java` |
| 新增 | `aop/ProceedingJoinPoint.java` |
| 修改 | `aop/ProxyFactoryBean.java` |
| 新增 | `test/aop/ThrowsAdviceTest.java` |
| 新增 | `test/aop/AroundAdviceTest.java` |

### 3.4 AOP 自动代理 (AutoProxy)

**现状**：必须通过 XML 配置 `ProxyFactoryBean` 手动创建代理。

**任务**：
- [ ] 创建 `@Aspect` 注解
- [ ] 创建 `@Before`、`@After`、`@Around`、`@AfterThrowing` 通知注解
- [ ] 创建 `@Pointcut` 注解（value 为方法名表达式）
- [ ] 创建 `AspectJAutoProxyBeanPostProcessor`：实现 `BeanPostProcessor`
  - `postProcessAfterInitialization` 中扫描容器内的 `@Aspect` bean
  - 解析其通知方法，构建 Advisor 列表
  - 遍历所有候选 bean，检查 Pointcut 匹配
  - 匹配时创建 JDK 动态代理替换原 bean

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `aop/annotation/Aspect.java` |
| 新增 | `aop/annotation/Before.java` |
| 新增 | `aop/annotation/After.java` |
| 新增 | `aop/annotation/Around.java` |
| 新增 | `aop/annotation/AfterThrowing.java` |
| 新增 | `aop/annotation/Pointcut.java` |
| 新增 | `aop/aspectj/AspectJAutoProxyBeanPostProcessor.java` |
| 新增 | `aop/aspectj/AspectJExpressionPointcut.java` |
| 新增 | `test/aop/AutoProxyTest.java` |

---

## Tier 4 — Web 层骨架

> 当前 `DispatcherServlet` 为空壳，`@RequestMapping` 未被任何代码处理。

### 4.1 HandlerMapping 实现

**任务**：
- [ ] 创建 `HandlerMapping` 接口（`getHandler(HttpServletRequest)`）
- [ ] 创建 `RequestMappingHandlerMapping`：
  - 扫描所有 `@Controller` / `@RequestMapping` 注解的 bean
  - 构建 `Map<String, HandlerMethod>`（URL → 方法映射）
  - 支持 `@RequestMapping(value="/path", method=GET)` HTTP 方法过滤

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `web/servlet/HandlerMapping.java` |
| 新增 | `web/servlet/HandlerMethod.java` |
| 新增 | `web/servlet/method/RequestMappingHandlerMapping.java` |
| 新增 | `test/web/RequestMappingHandlerMappingTest.java` |

### 4.2 HandlerAdapter 实现

**任务**：
- [ ] 创建 `HandlerAdapter` 接口（`handle(request, response, handler)`）
- [ ] 创建 `RequestMappingHandlerAdapter`：
  - 反射调用 Controller 方法
  - 处理 `@RequestParam` 参数绑定
  - 处理 `@PathVariable` 路径变量
  - 处理 `@ResponseBody` JSON 序列化（Jackson）

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `web/servlet/HandlerAdapter.java` |
| 新增 | `web/servlet/method/RequestMappingHandlerAdapter.java` |
| 新增 | `test/web/RequestMappingHandlerAdapterTest.java` |

### 4.3 DispatcherServlet 核心逻辑

**任务**：
- [ ] 实现 `init()`：通过 `WebApplicationContext` 获取 HandlerMapping / HandlerAdapter
- [ ] 实现 `doGet()` / `doPost()` / `doService()`：
  - URL 解析 → HandlerMapping 查找 → HandlerAdapter 调用 → 响应输出
- [ ] 实现 `WebApplicationContext`：扩展 `ApplicationContext`，持有 `ServletContext`
- [ ] 集成 `ViewResolver` 视图解析（支持 JSP / JSON）

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 修改 | `web/DispatcherServlet.java` |
| 新增 | `web/context/WebApplicationContext.java` |
| 新增 | `web/servlet/ViewResolver.java` |
| 新增 | `test/web/DispatcherServletTest.java` |

### 4.4 MVC 注解

**任务**：
- [ ] 创建 `@Controller` 注解（标记 Controller 组件）
- [ ] 创建 `@RestController` 注解（组合 @Controller + @ResponseBody）
- [ ] 创建 `@GetMapping` / `@PostMapping` / `@PutMapping` / `@DeleteMapping`
- [ ] 创建 `@RequestParam` 注解（请求参数绑定）
- [ ] 创建 `@PathVariable` 注解（路径变量）
- [ ] 创建 `@ResponseBody` 注解（JSON 响应）
- [ ] 修改 `@RequestMapping` 增加 `method` 属性

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `web/bind/annotation/Controller.java` |
| 新增 | `web/bind/annotation/RestController.java` |
| 新增 | `web/bind/annotation/GetMapping.java` |
| 新增 | `web/bind/annotation/PostMapping.java` |
| 新增 | `web/bind/annotation/PutMapping.java` |
| 新增 | `web/bind/annotation/DeleteMapping.java` |
| 新增 | `web/bind/annotation/RequestParam.java` |
| 新增 | `web/bind/annotation/PathVariable.java` |
| 新增 | `web/bind/annotation/ResponseBody.java` |
| 修改 | `web/RequestMapping.java` |

---

## Tier 5 — 容器增强

> 补齐 IoC 容器细节能力，覆盖更多 Spring 兼容场景。

### 5.1 @EventListener 注解

**任务**：
- [ ] 创建 `@EventListener` 注解（标记在 public 方法上）
- [ ] 创建 `EventListenerMethodProcessor`：BeanPostProcessor，在初始化后扫描方法
  - 根据方法参数类型（ApplicationEvent 子类）匹配事件
  - 注册为监听器适配器

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `context/event/EventListener.java` |
| 新增 | `context/event/EventListenerMethodProcessor.java` |
| 新增 | `test/event/EventListenerTest.java` |

### 5.2 destroy-method + DisposableBean

**任务**：
- [ ] 创建 `DisposableBean` 接口（`destroy()` 方法）
- [ ] 修改 `BeanDefinition` 增加 `destroyMethodName`
- [ ] 修改 `XmlBeanDefinitionReader` 支持 `<bean destroy-method="...">`
- [ ] 修改 `AbstractApplicationContext.close()` 遍历单例调用 `destroy()`
- [ ] 在 `createBean()` 中检查 `DisposableBean` 实例并注册到销毁列表

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `beans/factory/DisposableBean.java` |
| 修改 | `beans/factory/config/BeanDefinition.java` |
| 修改 | `beans/factory/support/AbstractBeanFactory.java` |
| 修改 | `beans/factory/xml/XmlBeanDefinitionReader.java` |
| 修改 | `context/AbstractApplicationContext.java` |
| 新增 | `test/beans/DisposableBeanTest.java` |

### 5.3 InitializingBean 接口

**任务**：
- [ ] 创建 `InitializingBean` 接口（`afterPropertiesSet()`）
- [ ] 在 `createBean()` 中 init-method 之前调用 `afterPropertiesSet()`

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `beans/factory/InitializingBean.java` |
| 修改 | `beans/factory/support/AbstractBeanFactory.java` |
| 新增 | `test/beans/InitializingBeanTest.java` |

### 5.4 补齐常用注解

**任务**：
- [ ] 创建 `@Scope` 注解（`@Scope("prototype")` — 类级别作用域标注）
- [ ] 创建 `@Primary` 注解 — 多候选时优先注入
- [ ] 创建 `@Lazy` 注解 — 延迟初始化
- [ ] 修改 `ClassPathComponentScanner` 支持 `@Scope` 读取
- [ ] 修改 `AutowiredAnnotationBeanPostProcessor` 支持 `@Primary`

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `context/annotation/Scope.java` |
| 新增 | `beans/factory/annotation/Primary.java` |
| 新增 | `context/annotation/Lazy.java` |
| 修改 | `core/scanner/ClassPathComponentScanner.java` |
| 修改 | `beans/factory/annotation/AutowiredAnnotationBeanPostProcessor.java` |
| 新增 | `test/beans/ScopeAnnotationTest.java` |
| 新增 | `test/beans/PrimaryAnnotationTest.java` |

### 5.5 属性类型扩展

**任务**：
- [ ] `handleProperties()` 增加 `long` / `boolean` / `double` / `float` 类型支持
- [ ] `doCreateBean()` 构造函数参数增加等价支持
- [ ] 移除现有 TODO 注释

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 修改 | `beans/factory/support/AbstractBeanFactory.java` |
| 新增 | `test/beans/PropertyTypeTest.java` |

### 5.6 Environment 实现

**任务**：
- [ ] 创建 `StandardEnvironment`：实现 `Environment` 接口
  - 加载 `System.getProperties()` + `System.getenv()`
  - 优先级：Java properties > env variables
- [ ] 创建 `@PropertySource` 注解加载外部 `.properties`
- [ ] 与 `ValueAnnotationBeanPostProcessor` 集成

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `core/env/StandardEnvironment.java` |
| 新增 | `context/annotation/PropertySource.java` |
| 修改 | `beans/factory/annotation/ValueAnnotationBeanPostProcessor.java` |
| 修改 | `context/AnnotationConfigApplicationContext.java` |
| 新增 | `test/env/EnvironmentTest.java` |

---

## Tier 6 — 验证模块增强

> 扩展 `summer-validator` 模块，增加更多约束注解并与 IoC 集成。

### 6.1 新增约束注解

**任务**：
- [ ] 创建 `@Size(min, max)` — 字符串长度 / 集合大小校验
- [ ] 创建 `@NotEmpty` — 非空校验
- [ ] 创建 `@Min(value)` / `@Max(value)` — 数值范围校验
- [ ] 创建对应的 `SizeProcessor` / `NotEmptyProcessor` / `MinProcessor` / `MaxProcessor`
- [ ] 注册到 `AnnotationProcessorRegister`

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `validator/annotations/Size.java` |
| 新增 | `validator/annotations/NotEmpty.java` |
| 新增 | `validator/annotations/Min.java` |
| 新增 | `validator/annotations/Max.java` |
| 新增 | `validator/constraintvalidators/SizeProcessor.java` |
| 新增 | `validator/constraintvalidators/NotEmptyProcessor.java` |
| 新增 | `validator/constraintvalidators/MinProcessor.java` |
| 新增 | `validator/constraintvalidators/MaxProcessor.java` |
| 修改 | `validator/processor/AnnotationProcessorRegister.java` |
| 新增 | `test/validator/ConstraintAnnotationTest.java` |

### 6.2 验证器与 IoC 集成

**任务**：
- [ ] 在 `summer-beans` 中创建 `ValidationBeanPostProcessor`
  - 扫描 bean 的约束注解（`@NotNull`、`@Size` 等）
  - 自动调用 `Validators` 进行校验
- [ ] `AnnotationConfigApplicationContext` 自动注册该处理器

**涉及文件**：
| 操作 | 文件 |
|---|---|
| 新增 | `beans/factory/validation/ValidationBeanPostProcessor.java` |
| 修改 | `context/AnnotationConfigApplicationContext.java` |
| 新增 | `test/validation/ValidationIntegrationTest.java` |

---

## 执行汇总

| Tier | 章节 | PR 数 | 预估测试数 | 优先级 |
|---|---|---|---|---|
| Tier 3 | AOP 完善 | 4 | ~20 | ⭐⭐⭐ |
| Tier 4 | Web 骨架 | 4 | ~20 | ⭐⭐⭐ |
| Tier 5 | 容器增强 | 6 | ~25 | ⭐⭐ |
| Tier 6 | 验证增强 | 2 | ~15 | ⭐ |
| **合计** | | **16** | **~80** | |

> 每个 Tier 内的 PR 按顺序依赖执行（如 3.2 依赖 3.1），不同 Tier 之间相互独立可并行。
