package com.dianpoint.summer.aop;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertSame;

public class JdkDynamicAopProxyTest {

    @Test
    public void testGetProxy_noAdvisor_returnsProxy() {
        Object target = new Object();
        JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(target, null);
        Object proxyObj = proxy.getProxy();
        assertThat(proxyObj).isNotNull();
    }

    @Test
    public void testInvoke_withNullAdvisor_delegatesToTarget() throws Throwable {
        SomeService target = new SomeServiceImpl();
        JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(target, null);
        Method method = SomeService.class.getMethod("doAction");
        Object result = proxy.invoke(null, method, null);
        assertThat(result).isEqualTo("real");
    }

    @Test
    public void testInvoke_withNullMethodInterceptor_delegatesToTarget() throws Throwable {
        SomeService target = new SomeServiceImpl();
        Advisor advisor = new DefaultAdvisor();
        JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(target, advisor);
        Method method = SomeService.class.getMethod("doAction");
        Object result = proxy.invoke(null, method, null);
        assertThat(result).isEqualTo("real");
    }

    interface SomeService {
        String doAction();
    }

    static class SomeServiceImpl implements SomeService {
        @Override
        public String doAction() {
            return "real";
        }
    }
}
