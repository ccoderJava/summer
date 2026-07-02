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
            // 没有配置拦截器时，直接委托给目标对象
            return method.invoke(target, args);
        }
        Class<?> targetClass = target != null ? target.getClass() : null;
        MethodInterceptor interceptor = this.advisor.getMethodInterceptor();
        ReflectiveMethodInvocation invocation =
            new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
        return interceptor.invoke(invocation);
    }
}
