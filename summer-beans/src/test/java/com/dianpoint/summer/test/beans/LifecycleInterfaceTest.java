package com.dianpoint.summer.test.beans;

import static org.assertj.core.api.Assertions.assertThat;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.DisposableBean;
import com.dianpoint.summer.beans.factory.InitializingBean;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class LifecycleInterfaceTest {

    public static class InitBean implements InitializingBean {
        private boolean afterPropertiesSetCalled = false;

        @Override
        public void afterPropertiesSet() throws Exception {
            afterPropertiesSetCalled = true;
        }

        public boolean isAfterPropertiesSetCalled() {
            return afterPropertiesSetCalled;
        }
    }

    public static class DestroyBean implements DisposableBean {
        private final AtomicBoolean destroyed = new AtomicBoolean(false);

        @Override
        public void destroy() throws Exception {
            destroyed.set(true);
        }

        public boolean isDestroyed() {
            return destroyed.get();
        }
    }

    public static class DestroyMethodBean {
        private boolean cleanedUp = false;

        public void cleanup() {
            cleanedUp = true;
        }

        public boolean isCleanedUp() {
            return cleanedUp;
        }
    }

    @Test
    public void testInitializingBeanAfterPropertiesSetCalled() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition("initBean",
            new BeanDefinition("initBean", InitBean.class.getName()));

        InitBean bean = (InitBean) factory.getBean("initBean");
        assertThat(bean.isAfterPropertiesSetCalled()).isTrue();
    }

    @Test
    public void testDisposableBeanDestroyed() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition("destroyBean",
            new BeanDefinition("destroyBean", DestroyBean.class.getName()));

        DestroyBean bean = (DestroyBean) factory.getBean("destroyBean");
        assertThat(bean.isDestroyed()).isFalse();

        factory.destroySingletons();
        assertThat(bean.isDestroyed()).isTrue();
    }

    @Test
    public void testDestroyMethodInvoked() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition bd = new BeanDefinition("dmBean", DestroyMethodBean.class.getName());
        bd.setDestroyMethodName("cleanup");
        factory.registerBeanDefinition("dmBean", bd);

        DestroyMethodBean bean = (DestroyMethodBean) factory.getBean("dmBean");
        assertThat(bean.isCleanedUp()).isFalse();

        factory.destroySingletons();
        assertThat(bean.isCleanedUp()).isTrue();
    }

    @Test
    public void testInitMethodAfterInitializingBean() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition bd = new BeanDefinition("initBean", InitBean.class.getName());
        bd.setInitMethodName("toString");
        factory.registerBeanDefinition("initBean", bd);

        InitBean bean = (InitBean) factory.getBean("initBean");
        assertThat(bean.isAfterPropertiesSetCalled()).isTrue();
    }
}
