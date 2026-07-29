package com.dianpoint.summer.aop;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectiveMethodInvocationTest {

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method method = String.class.getMethod("length");
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation("proxy", "target", method, new Object[0], String.class);
        assertThat(invocation.getMethod()).isEqualTo(method);
    }

    @Test
    public void testGetArguments() throws NoSuchMethodException {
        Object[] args = new Object[]{"arg1", 42};
        Method method = String.class.getMethod("substring", int.class);
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation("proxy", "target", method, args, String.class);
        assertThat(invocation.getArguments()).isEqualTo(args);
    }

    @Test
    public void testGetThis_returnsTarget() throws NoSuchMethodException {
        String target = "hello";
        Method method = String.class.getMethod("length");
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation("proxy", target, method, new Object[0], String.class);
        assertThat(invocation.getThis()).isEqualTo(target);
    }

    @Test
    public void testGetProxy_returnsProxy() throws NoSuchMethodException {
        Object proxy = new Object();
        Method method = String.class.getMethod("length");
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(proxy, "target", method, new Object[0], String.class);
        assertThat(invocation.getProxy()).isEqualTo(proxy);
    }

    @Test
    public void testGetTarget_returnsTarget() throws NoSuchMethodException {
        Object target = "hello";
        Method method = String.class.getMethod("length");
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation("proxy", target, method, new Object[0], String.class);
        assertThat(invocation.getTarget()).isEqualTo(target);
    }

    @Test
    public void testGetTargetClass() throws NoSuchMethodException {
        Method method = String.class.getMethod("length");
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation("proxy", "target", method, new Object[0], String.class);
        assertThat(invocation.getTargetClass()).isEqualTo(String.class);
    }

    @Test
    public void testSetTargetClass() throws NoSuchMethodException {
        Method method = String.class.getMethod("length");
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation("proxy", "target", method, new Object[0], String.class);
        invocation.setTargetClass(Object.class);
        assertThat(invocation.getTargetClass()).isEqualTo(Object.class);
    }

    @Test
    public void testProceed_invokesMethod() throws Throwable {
        String target = "hello";
        Method method = String.class.getMethod("length");
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation("proxy", target, method, new Object[0], String.class);
        Object result = invocation.proceed();
        assertThat(result).isEqualTo(5);
    }

    @Test
    public void testProceed_invokesMethodWithArgs() throws Throwable {
        String target = "hello world";
        Method method = String.class.getMethod("substring", int.class, int.class);
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation("proxy", target, method, new Object[]{0, 5}, String.class);
        Object result = invocation.proceed();
        assertThat(result).isEqualTo("hello");
    }
}
