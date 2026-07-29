package com.dianpoint.summer.aop;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultAopProxyFactoryTest {

    @Test
    public void testCreateAopProxy_returnsJdkDynamicAopProxy() {
        DefaultAopProxyFactory factory = new DefaultAopProxyFactory();
        Object target = new Object();
        Advisor advisor = new DefaultAdvisor();
        AopProxy proxy = factory.createAopProxy(target, advisor);
        assertThat(proxy).isInstanceOf(JdkDynamicAopProxy.class);
    }
}
