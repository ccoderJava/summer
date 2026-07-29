package com.dianpoint.summer.aop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class AroundAdviceTest {

    public interface Calculator {
        int compute(int a, int b);
    }

    public static class CalculatorImpl implements Calculator {
        @Override
        public int compute(int a, int b) {
            return a + b;
        }
    }

    public static class TimingAroundAdvice implements AroundAdvice {
        private final AtomicBoolean invoked = new AtomicBoolean(false);
        private long elapsed = 0;

        @Override
        public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
            invoked.set(true);
            long start = System.nanoTime();
            Object result = joinPoint.proceed();
            elapsed = System.nanoTime() - start;
            return result;
        }

        public boolean isInvoked() {
            return invoked.get();
        }

        public long getElapsed() {
            return elapsed;
        }
    }

    public static class ModifyingAroundAdvice implements AroundAdvice {
        @Override
        public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
            Integer result = (Integer) joinPoint.proceed();
            return result * 2;
        }
    }

    @Test
    public void testAroundAdviceWrapsProceed() {
        TimingAroundAdvice advice = new TimingAroundAdvice();
        AroundAdviceInterceptor interceptor = new AroundAdviceInterceptor(advice);

        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(interceptor);

        CalculatorImpl target = new CalculatorImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Calculator proxy = (Calculator) aopProxy.getProxy();

        int result = proxy.compute(1, 2);
        assertThat(result).isEqualTo(3);
        assertThat(advice.isInvoked()).isTrue();
        assertThat(advice.getElapsed()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void testAroundAdviceModifiesResult() {
        ModifyingAroundAdvice advice = new ModifyingAroundAdvice();
        AroundAdviceInterceptor interceptor = new AroundAdviceInterceptor(advice);

        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(interceptor);

        CalculatorImpl target = new CalculatorImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Calculator proxy = (Calculator) aopProxy.getProxy();

        int result = proxy.compute(1, 2);
        assertThat(result).isEqualTo(6);
    }

    @Test
    public void testAroundAdviceProceedingJoinPoint() throws Throwable {
        AroundAdvice advice = joinPoint -> {
            assertThat(joinPoint.getMethod().getName()).isEqualTo("compute");
            assertThat(joinPoint.getArgs()).containsExactly(1, 2);
            assertThat(joinPoint.getTarget()).isInstanceOf(CalculatorImpl.class);
            return joinPoint.proceed();
        };
        AroundAdviceInterceptor interceptor = new AroundAdviceInterceptor(advice);

        DefaultAdvisor advisor = new DefaultAdvisor();
        advisor.addMethodInterceptor(interceptor);

        CalculatorImpl target = new CalculatorImpl();
        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(target, advisor);
        Calculator proxy = (Calculator) aopProxy.getProxy();

        int result = proxy.compute(1, 2);
        assertThat(result).isEqualTo(3);
    }
}
