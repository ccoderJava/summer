package com.dianpoint.summer.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK Dynamic Proxy 实现AOP
 *
 * @author: github/ccoderJava
 * @email: congccoder@gmail.com
 * @date: 2023/3/26 21:04
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    Object target;
    Advisor advisor;

    public JdkDynamicAopProxy(Object target, Advisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.advisor == null || this.advisor.getMethodInterceptor() == null) {
            return method.invoke(target, args);
        }
        Class<?> targetClass = target != null ? target.getClass() : null;

        Pointcut pointcut = this.advisor.getPointcut();
        if (pointcut != null && !pointcut.matches(method, targetClass)) {
            return method.invoke(target, args);
        }

        ReflectiveMethodInvocation invocation =
            new ReflectiveMethodInvocation(proxy, target, method, args, targetClass,
                this.advisor.getMethodInterceptors());
        return invocation.proceed();
    }
}
