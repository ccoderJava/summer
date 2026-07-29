package com.dianpoint.summer.aop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class PointcutProxyTest {

    public interface Calculator {
        int add(int a, int b);
        int subtract(int a, int b);
    }

    public static class CalculatorImpl implements Calculator {
        @Override
        public int add(int a, int b) {
            return a + b;
        }

        @Override
        public int subtract(int a, int b) {
            return a - b;
        }
    }

    public static class LoggingAdvice implements MethodBeforeAdvice {
        private final AtomicBoolean invoked = new AtomicBoolean(false);

        @Override
        public void before(java.lang.reflect.Method method, Object[] args, Object target) throws Throwable {
            invoked.set(true);
        }

        public boolean isInvoked() {
            return invoked.get();
        }
    }

    @Test
    public void testPointcutMatchesAndInterceptorInvoked() {
        LoggingAdvice advice = new LoggingAdvice();
        MethodBeforeAdviceInterceptor interceptor = new MethodBeforeAdviceInterceptor(advice);

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut("add");
        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.setMethodInterceptor(interceptor);
        advisor.setPointcut(pointcut);

        CalculatorImpl target = new CalculatorImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Calculator proxy = (Calculator) aopProxy.getProxy();

        int result = proxy.add(1, 2);
        assertThat(result).isEqualTo(3);
        assertThat(advice.isInvoked()).isTrue();
    }

    @Test
    public void testPointcutDoesNotMatchAndInterceptorSkipped() {
        LoggingAdvice advice = new LoggingAdvice();
        MethodBeforeAdviceInterceptor interceptor = new MethodBeforeAdviceInterceptor(advice);

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut("add");
        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.setMethodInterceptor(interceptor);
        advisor.setPointcut(pointcut);

        CalculatorImpl target = new CalculatorImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Calculator proxy = (Calculator) aopProxy.getProxy();

        int result = proxy.subtract(5, 3);
        assertThat(result).isEqualTo(2);
        assertThat(advice.isInvoked()).isFalse();
    }

    @Test
    public void testNoPointcutMeansMatchAll() {
        LoggingAdvice advice = new LoggingAdvice();
        MethodBeforeAdviceInterceptor interceptor = new MethodBeforeAdviceInterceptor(advice);

        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.setMethodInterceptor(interceptor);

        CalculatorImpl target = new CalculatorImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Calculator proxy = (Calculator) aopProxy.getProxy();

        proxy.add(1, 2);
        assertThat(advice.isInvoked()).isTrue();
    }
}
