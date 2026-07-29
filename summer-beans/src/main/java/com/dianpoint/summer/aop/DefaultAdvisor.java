package com.dianpoint.summer.aop;

import java.util.ArrayList;
import java.util.List;

public class DefaultAdvisor implements Advisor {

    private final List<MethodInterceptor> interceptors = new ArrayList<>();
    private Pointcut pointcut;

    @Override
    public MethodInterceptor getMethodInterceptor() {
        return interceptors.isEmpty() ? null : interceptors.get(0);
    }

    @Override
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.interceptors.clear();
        this.interceptors.add(methodInterceptor);
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors() {
        return interceptors;
    }

    @Override
    public void addMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.interceptors.add(methodInterceptor);
    }

    @Override
    public Advice getAdvice() {
        return getMethodInterceptor();
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }
}
