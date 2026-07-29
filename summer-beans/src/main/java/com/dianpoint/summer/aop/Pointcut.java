package com.dianpoint.summer.aop;

import java.lang.reflect.Method;

public interface Pointcut {

    boolean matches(Method method, Class<?> targetClass);
}
