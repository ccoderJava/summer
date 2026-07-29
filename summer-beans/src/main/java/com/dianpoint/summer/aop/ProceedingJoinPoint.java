package com.dianpoint.summer.aop;

import java.lang.reflect.Method;

public class ProceedingJoinPoint {

    private final MethodInvocation invocation;

    public ProceedingJoinPoint(MethodInvocation invocation) {
        this.invocation = invocation;
    }

    public Object proceed() throws Throwable {
        return invocation.proceed();
    }

    public Method getMethod() {
        return invocation.getMethod();
    }

    public Object[] getArgs() {
        return invocation.getArguments();
    }

    public Object getTarget() {
        return invocation.getThis();
    }
}
