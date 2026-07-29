package com.dianpoint.summer.aop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class ThrowsAdviceTest {

    public interface Divider {
        int divide(int a, int b);
    }

    public static class DividerImpl implements Divider {
        @Override
        public int divide(int a, int b) {
            if (b == 0) {
                throw new ArithmeticException("division by zero");
            }
            return a / b;
        }
    }

    public static class LoggingThrowsAdvice implements ThrowsAdvice {
        private final AtomicReference<Throwable> caught = new AtomicReference<>();

        @Override
        public void afterThrowing(Method method, Object[] args, Object target, Throwable exception) throws Throwable {
            caught.set(exception);
        }

        public Throwable getCaught() {
            return caught.get();
        }
    }

    @Test
    public void testThrowsAdviceCatchesException() {
        LoggingThrowsAdvice throwsAdvice = new LoggingThrowsAdvice();
        AfterThrowingAdviceInterceptor interceptor = new AfterThrowingAdviceInterceptor(throwsAdvice);

        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(interceptor);

        DividerImpl target = new DividerImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Divider proxy = (Divider) aopProxy.getProxy();

        assertThatThrownBy(() -> proxy.divide(10, 0))
            .isInstanceOf(ArithmeticException.class);

        assertThat(throwsAdvice.getCaught()).isNotNull();
        assertThat(throwsAdvice.getCaught()).isInstanceOf(ArithmeticException.class);
    }

    @Test
    public void testThrowsAdviceNotCalledOnSuccess() {
        LoggingThrowsAdvice throwsAdvice = new LoggingThrowsAdvice();
        AfterThrowingAdviceInterceptor interceptor = new AfterThrowingAdviceInterceptor(throwsAdvice);

        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(interceptor);

        DividerImpl target = new DividerImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Divider proxy = (Divider) aopProxy.getProxy();

        int result = proxy.divide(10, 2);
        assertThat(result).isEqualTo(5);
        assertThat(throwsAdvice.getCaught()).isNull();
    }

    @Test
    public void testThrowsAdviceReThrowsException() {
        ThrowsAdvice rethrow = (method, args, target, ex) -> {
            throw new RuntimeException("wrapped", ex);
        };
        AfterThrowingAdviceInterceptor interceptor = new AfterThrowingAdviceInterceptor(rethrow);

        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(interceptor);

        DividerImpl target = new DividerImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Divider proxy = (Divider) aopProxy.getProxy();

        assertThatThrownBy(() -> proxy.divide(10, 0))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("wrapped");
    }
}
