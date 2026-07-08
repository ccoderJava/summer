package com.dianpoint.summer.aop;

import java.lang.reflect.Method;

/**
 * @deprecated 与 {@link AfterReturningAdvice} 功能重复，请使用 {@link AfterReturningAdvice}
 */
@Deprecated
public interface MethodReturningAdvice extends AfterAdvice{

    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;
}
