# Summer Framework 优化路线图

> 基于代码深度分析的增量开发计划，按 Tier 依次执行。已全部完成。

---

## 已完成 PR 汇总 (共 17 个)

| PR | Tier | 内容 | 链接 |
|---|---|---|---|
| #32 | T1 | Prototype scope 支持 | [PR #32](https://github.com/dianpoint/summer/pull/32) |
| #33 | T1 | @PostConstruct / @PreDestroy 生命周期 | [PR #33](https://github.com/dianpoint/summer/pull/33) |
| #34 | T1 | BeanFactoryPostProcessor 调用 | [PR #34](https://github.com/dianpoint/summer/pull/34) |
| #35 | T1 | @Value 属性注入 + 占位符解析 | [PR #35](https://github.com/dianpoint/summer/pull/35) |
| #36 | T1 | ApplicationListener 接口重构 | [PR #36](https://github.com/dianpoint/summer/pull/36) |
| #37 | T2 | @Component + ComponentScan | [PR #37](https://github.com/dianpoint/summer/pull/37) |
| #38 | T2 | @Configuration + @Bean 注解配置 | [PR #38](https://github.com/dianpoint/summer/pull/38) |
| #39 | T2 | @Qualifier 限定注入 | [PR #39](https://github.com/dianpoint/summer/pull/39) |
| #40 | T3 | Pointcut 接口 + NameMatchMethodPointcut | [PR #40](https://github.com/dianpoint/summer/pull/40) |
| #41 | T3 | 多拦截器链 | [PR #41](https://github.com/dianpoint/summer/pull/41) |
| #42 | T3 | ThrowsAdvice + AroundAdvice | [PR #42](https://github.com/dianpoint/summer/pull/42) |
| #43 | T3 | @Aspect 自动代理 | [PR #43](https://github.com/dianpoint/summer/pull/43) |
| #44 | T4 | Web MVC 层 (HandlerMapping + DispatcherServlet) | [PR #44](https://github.com/dianpoint/summer/pull/44) |
| #45 | T5 | @EventListener 注解 | [PR #45](https://github.com/dianpoint/summer/pull/45) |
| #46 | T5 | DisposableBean + InitializingBean + destroy-method | [PR #46](https://github.com/dianpoint/summer/pull/46) |
| #47 | T5 | @Scope + @Primary + @Lazy 注解 | [PR #47](https://github.com/dianpoint/summer/pull/47) |
| #48 | T5+T6 | Property 类型扩展 + Environment + 验证注解 | [PR #48](https://github.com/dianpoint/summer/pull/48) |

---

## 各 Tier 功能清单

### Tier 1 — IoC 容器增强 (PR #32–#36)

| 功能 | 文件 | 说明 |
|---|---|---|
| Prototype scope | `AbstractBeanFactory.java` | getBean() 分支处理 prototype，refresh() 跳过原型 bean |
| @PostConstruct | `PostConstruct.java` + `InitDestroyAnnotationBeanPostProcessor.java` | 注解式 init 回调 |
| @PreDestroy | `PreDestroy.java` + `ClassPathXmlApplicationContext.close()` | 注解式 destroy 回调 |
| BeanFactoryPostProcessor | `ClassPathXmlApplicationContext.java` | refresh() 中执行已注册处理器 |
| @Value + \${} | `Value.java` + `ValueAnnotationBeanPostProcessor.java` | 属性占位符注入 |
| ApplicationListener | `ApplicationListener.java` | 改为泛型接口 |

### Tier 2 — 注解配置 (PR #37–#39)

| 功能 | 文件 | 说明 |
|---|---|---|
| @Component | `Component.java` + `ClassPathComponentScanner.java` | 类路径扫描 |
| AnnotationConfigApplicationContext | `AnnotationConfigApplicationContext.java` | 零 XML 容器 |
| @Configuration + @Bean | `Configuration.java` + `Bean.java` + `ConfigurationClassBeanPostProcessor.java` | 注解式配置类 |
| @Qualifier | `Qualifier.java` + `AutowiredAnnotationBeanPostProcessor.java` | 限定注入 |

### Tier 3 — AOP 完善 (PR #40–#43)

| 功能 | 文件 | 说明 |
|---|---|---|
| Pointcut + 方法名匹配 | `Pointcut.java` + `NameMatchMethodPointcut.java` | 通配符 `do*` 等 |
| 多拦截器链 | `Advisor.java` + `ReflectiveMethodInvocation.java` | List\<MethodInterceptor\> 链式调用 |
| ThrowsAdvice + AroundAdvice | `ThrowsAdvice.java` + `AroundAdvice.java` | 异常通知 + 环绕通知 |
| @Aspect 自动代理 | `@Aspect` + `AspectJAutoProxyBeanPostProcessor.java` | 注解驱动 AOP |

### Tier 4 — Web 层 (PR #44)

| 功能 | 文件 | 说明 |
|---|---|---|
| HandlerMapping | `HandlerMapping.java` + `RequestMappingHandlerMapping.java` | URL 映射 |
| HandlerAdapter | `HandlerAdapter.java` + `RequestMappingHandlerAdapter.java` | 参数绑定 + JSON 响应 |
| DispatcherServlet | `DispatcherServlet.java` | doDispatch() 流程 |
| MVC 注解 | `@Controller` + `@RequestParam` + `@ResponseBody` | Controller 注解体系 |

### Tier 5 — 容器增强 (PR #45–#48)

| 功能 | 文件 | 说明 |
|---|---|---|
| @EventListener | `EventListener.java` + `EventListenerMethodProcessor.java` | 声明式事件监听 |
| InitializingBean | `InitializingBean.java` | afterPropertiesSet() 回调 |
| DisposableBean + destroy-method | `DisposableBean.java` + `destroySingletons()` | 销毁生命周期 |
| @Scope / @Primary / @Lazy | `Scope.java` + `Primary.java` + `Lazy.java` | 补充常用注解 |
| Property 类型扩展 | `AbstractBeanFactory.java` | long/boolean/double/float 支持 |
| StandardEnvironment | `StandardEnvironment.java` | Environment 接口实现 |

### Tier 6 — 验证模块 (PR #48)

| 功能 | 文件 | 说明 |
|---|---|---|
| @Size / @NotEmpty | `Size.java` + `NotEmpty.java` | 长度/非空约束 |
| @Min / @Max | `Min.java` + `Max.java` | 数值范围约束 |

---

## 架构全景

```
summer (IoC/AOP/MVC 轻量框架)
├── summer-parent          (BOM 依赖管理)
├── summer-java-core       (工具类: Wrapper, Streams, Predicates, AssertUtils)
├── summer-validator       (校验: @NotNull, @Pattern, @Size, @NotEmpty, @Min, @Max)
└── summer-beans           (核心容器)
    ├── beans/factory      (IoC: BeanFactory, BeanDefinition, Scope)
    ├── beans/factory/annotation  (@Autowired, @Qualifier, @Value, @PostConstruct)
    ├── beans/factory/config      (AbstractAutowireCapableBeanFactory, BeanPostProcessor)
    ├── beans/factory/support     (DefaultListableBeanFactory)
    ├── beans/factory/xml         (XML 配置解析)
    ├── aop                (AOP: Pointcut, Advisor, Advice, @Aspect 自动代理)
    ├── context            (ApplicationContext, 事件, @Configuration, @Bean)
    ├── context/annotation (@Scope, @Lazy, @EventListener)
    ├── core/env           (Environment, StandardEnvironment)
    ├── core/scanner       (ClassPathScanner, Component 扫描)
    ├── stereotype         (@Component, @Controller)
    ├── web                (DispatcherServlet, @RequestMapping)
    └── web/servlet        (HandlerMapping, HandlerAdapter)
```

---

## 累计产出

| 指标 | 数值 |
|---|---|
| 新增/修改文件 | ~120 个 |
| 新增测试方法 | ~170 个 |
| PR 总数 | 17 个 |
| 覆盖 Tier | 0–6 (全部完成) |
