package com.dianpoint.summer.aop;

import java.lang.reflect.Method;

public interface ThrowsAdvice extends AfterAdvice {

    void afterThrowing(Method method, Object[] args, Object target, Throwable exception) throws Throwable;
}
