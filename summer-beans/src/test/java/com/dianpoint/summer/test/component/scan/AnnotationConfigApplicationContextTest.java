package com.dianpoint.summer.test.component.scan;

import static org.assertj.core.api.Assertions.assertThat;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.context.AnnotationConfigApplicationContext;
import org.junit.Test;

public class AnnotationConfigApplicationContextTest {

    @Test
    public void testApplicationContextScansAndCreatesBeans() throws BeansException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
            "com.dianpoint.summer.test.component.scan");

        GreetingService greeting = (GreetingService) ctx.getBean("greetingService");
        EchoService echo = (EchoService) ctx.getBean("echoService");

        assertThat(greeting).isNotNull();
        assertThat(echo).isNotNull();
        assertThat(greeting.greet("World")).isEqualTo("Hello, World");
        assertThat(echo.echo("test")).isEqualTo("test");
    }

    @Test
    public void testLifecycleAnnotationsWithComponentScan() throws BeansException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
            "com.dianpoint.summer.test.component.scan");

        LifecycleComponent lifecycle = (LifecycleComponent) ctx.getBean("lifecycleComponent");
        assertThat(lifecycle.isPostConstructCalled()).isTrue();
        assertThat(lifecycle.isPreDestroyCalled()).isFalse();

        ctx.close();
        assertThat(lifecycle.isPreDestroyCalled()).isTrue();
    }

    @Test
    public void testBeanIsSingletonByDefault() throws BeansException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
            "com.dianpoint.summer.test.component.scan");

        EchoService echo1 = (EchoService) ctx.getBean("echoService");
        EchoService echo2 = (EchoService) ctx.getBean("echoService");

        assertThat(echo1).isSameAs(echo2);
    }
}
