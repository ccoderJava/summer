package com.dianpoint.summer.aop;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class AfterReturningAdviceInterceptorTest {

    @Test
    public void testInvoke_callsProceed_thenAfterReturning() throws Throwable {
        final AtomicBoolean afterCalled = new AtomicBoolean(false);
        final String target = "test";

        AfterReturningAdvice advice = new AfterReturningAdvice() {
            @Override
            public void afterReturning(Object returnValue, Method method, Object[] args, Object targetObj) throws Throwable {
                afterCalled.set(true);
                assertThat(returnValue).isEqualTo("result");
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

        AfterReturningAdviceInterceptor interceptor = new AfterReturningAdviceInterceptor(advice);
        Object result = interceptor.invoke(invocation);

        assertThat(afterCalled.get()).isTrue();
        assertThat(result).isEqualTo("result");
    }
}
