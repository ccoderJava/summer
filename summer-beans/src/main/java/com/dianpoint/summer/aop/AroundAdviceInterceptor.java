package com.dianpoint.summer.aop;

public class AroundAdviceInterceptor implements MethodInterceptor {

    private final AroundAdvice advice;

    public AroundAdviceInterceptor(AroundAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ProceedingJoinPoint joinPoint = new ProceedingJoinPoint(invocation);
        return advice.around(joinPoint);
    }
}
