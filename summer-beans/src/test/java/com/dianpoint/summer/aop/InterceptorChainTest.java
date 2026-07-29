package com.dianpoint.summer.aop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class InterceptorChainTest {

    public interface Greeting {
        String sayHello(String name);
    }

    public static class GreetingImpl implements Greeting {
        @Override
        public String sayHello(String name) {
            return "Hello, " + name;
        }
    }

    public static class UppercaseInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String result = (String) invocation.proceed();
            return result.toUpperCase();
        }
    }

    public static class PrefixInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String result = (String) invocation.proceed();
            return "[PREFIX] " + result;
        }
    }

    public static class RecordingInterceptor implements MethodInterceptor {
        private final List<String> calls = new ArrayList<>();

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            calls.add("before");
            Object result = invocation.proceed();
            calls.add("after");
            return result;
        }

        public List<String> getCalls() {
            return calls;
        }
    }

    @Test
    public void testSingleInterceptorChain() {
        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(new UppercaseInterceptor());

        GreetingImpl target = new GreetingImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Greeting proxy = (Greeting) aopProxy.getProxy();

        String result = proxy.sayHello("World");
        assertThat(result).isEqualTo("HELLO, WORLD");
    }

    @Test
    public void testMultipleInterceptorChain() {
        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(new PrefixInterceptor());
        advisor.addMethodInterceptor(new UppercaseInterceptor());

        GreetingImpl target = new GreetingImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Greeting proxy = (Greeting) aopProxy.getProxy();

        String result = proxy.sayHello("World");
        assertThat(result).isEqualTo("[PREFIX] HELLO, WORLD");
    }

    @Test
    public void testInterceptorChainOrder() {
        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(new UppercaseInterceptor());
        advisor.addMethodInterceptor(new PrefixInterceptor());

        GreetingImpl target = new GreetingImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Greeting proxy = (Greeting) aopProxy.getProxy();

        String result = proxy.sayHello("World");
        assertThat(result).isEqualTo("[PREFIX] HELLO, WORLD");
    }

    @Test
    public void testInterceptorCallsProceed() {
        RecordingInterceptor recorder = new RecordingInterceptor();
        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(recorder);

        GreetingImpl target = new GreetingImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Greeting proxy = (Greeting) aopProxy.getProxy();

        proxy.sayHello("World");
        assertThat(recorder.getCalls()).containsExactly("before", "after");
    }
}
