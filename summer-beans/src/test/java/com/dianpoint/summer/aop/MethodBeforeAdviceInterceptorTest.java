package com.dianpoint.summer.aop;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodBeforeAdviceInterceptorTest {

    @Test
    public void testInvoke_callsBefore_thenProceed() throws Throwable {
        final AtomicBoolean beforeCalled = new AtomicBoolean(false);
        final String target = "test";

        MethodBeforeAdvice advice = new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object targetObj) throws Throwable {
                beforeCalled.set(true);
                assertThat(method.getName()).isEqualTo("toString");
                assertThat(targetObj).isEqualTo(target);
            }
        };

        final Method toStringMethod;
        try {
            toStringMethod = Object.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        MethodInvocation invocation = new MethodInvocation() {
            @Override
            public Method getMethod() {
                return toStringMethod;
            }

            @Override
            public Object[] getArguments() {
                return new Object[0];
            }

            @Override
            public Object getThis() {
                return target;
            }

            @Override
            public Object proceed() throws Throwable {
                return "result";
            }
        };

        MethodBeforeAdviceInterceptor interceptor = new MethodBeforeAdviceInterceptor(advice);
        Object result = interceptor.invoke(invocation);

        assertThat(beforeCalled.get()).isTrue();
        assertThat(result).isEqualTo("result");
    }
}
