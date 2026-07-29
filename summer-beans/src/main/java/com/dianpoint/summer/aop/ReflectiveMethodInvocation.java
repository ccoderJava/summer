package com.dianpoint.summer.aop;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class ReflectiveMethodInvocation implements MethodInvocation {

    private final Object proxy;
    private final Object target;
    private final Object[] arguments;
    private final Method method;
    private Class<?> targetClass;
    private final List<MethodInterceptor> interceptors;
    private int currentIndex = 0;

    public ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
            Class<?> targetClass) {
        this(proxy, target, method, arguments, targetClass,
            Collections.<MethodInterceptor>emptyList());
    }

    public ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
            Class<?> targetClass, List<MethodInterceptor> interceptors) {
        this.proxy = proxy;
        this.target = target;
        this.arguments = arguments;
        this.targetClass = targetClass;
        this.method = method;
        this.interceptors = interceptors;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    public Object getProxy() {
        return proxy;
    }

    public Object getTarget() {
        return target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public Object proceed() throws Throwable {
        if (currentIndex < interceptors.size()) {
            MethodInterceptor interceptor = interceptors.get(currentIndex++);
            return interceptor.invoke(this);
        }
        return this.method.invoke(this.target, this.arguments);
    }
}
