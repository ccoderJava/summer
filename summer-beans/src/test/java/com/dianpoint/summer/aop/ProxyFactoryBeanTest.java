package com.dianpoint.summer.aop;

import com.dianpoint.summer.beans.factory.BeanFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyFactoryBeanTest {

    @Test
    public void testDefaultConstructor_initsDefaultAopProxyFactory() {
        ProxyFactoryBean bean = new ProxyFactoryBean();
        assertThat(bean.getAopProxyFactory()).isInstanceOf(DefaultAopProxyFactory.class);
    }

    @Test
    public void testGetObjectType_returnsNull() {
        ProxyFactoryBean bean = new ProxyFactoryBean();
        assertThat(bean.getObjectType()).isNull();
    }

    @Test
    public void testSetAndGetTarget() {
        ProxyFactoryBean bean = new ProxyFactoryBean();
        Object target = new Object();
        bean.setTarget(target);
        assertThat(bean.getTarget()).isSameAs(target);
    }

    @Test
    public void testSetAndGetAopProxyFactory() {
        ProxyFactoryBean bean = new ProxyFactoryBean();
        AopProxyFactory customFactory = new DefaultAopProxyFactory();
        bean.setAopProxyFactory(customFactory);
        assertThat(bean.getAopProxyFactory()).isSameAs(customFactory);
    }
}
