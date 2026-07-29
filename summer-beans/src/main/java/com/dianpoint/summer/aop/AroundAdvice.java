package com.dianpoint.summer.aop;

public interface AroundAdvice extends Interceptor {

    Object around(ProceedingJoinPoint joinPoint) throws Throwable;
}
