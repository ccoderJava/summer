package com.dianpoint.summer.aop;

import java.util.List;

public interface Advisor {

    MethodInterceptor getMethodInterceptor();

    void setMethodInterceptor(MethodInterceptor methodInterceptor);

    List<MethodInterceptor> getMethodInterceptors();

    void addMethodInterceptor(MethodInterceptor methodInterceptor);

    Advice getAdvice();

    Pointcut getPointcut();

    void setPointcut(Pointcut pointcut);
}
