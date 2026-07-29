package com.dianpoint.summer.aop;

public class AfterThrowingAdviceInterceptor implements MethodInterceptor {

    private final ThrowsAdvice advice;

    public AfterThrowingAdviceInterceptor(ThrowsAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable ex) {
            advice.afterThrowing(invocation.getMethod(), invocation.getArguments(),
                invocation.getThis(), ex);
            throw ex;
        }
    }
}
