package com.dianpoint.summer.test.beans;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
import com.dianpoint.summer.beans.factory.annotation.PostConstruct;
import com.dianpoint.summer.beans.factory.annotation.PreDestroy;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LifecycleAnnotationTest {

    public static class PostConstructTestBean {
        private boolean postConstructCalled = false;

        @PostConstruct
        public void init() {
            postConstructCalled = true;
        }

        public boolean isPostConstructCalled() {
            return postConstructCalled;
        }
    }

    public static class PreDestroyTestBean {
        private boolean preDestroyCalled = false;

        @PreDestroy
        public void cleanup() {
            preDestroyCalled = true;
        }

        public boolean isPreDestroyCalled() {
            return preDestroyCalled;
        }
    }

    public static class FullLifecycleBean {
        private boolean postConstructCalled = false;
        private boolean initMethodCalled = false;
        private boolean preDestroyCalled = false;

        @PostConstruct
        public void init() {
            postConstructCalled = true;
        }

        @PreDestroy
        public void cleanup() {
            preDestroyCalled = true;
        }

        public void customInit() {
            initMethodCalled = true;
        }

        public boolean isPostConstructCalled() {
            return postConstructCalled;
        }

        public boolean isInitMethodCalled() {
            return initMethodCalled;
        }

        public boolean isPreDestroyCalled() {
            return preDestroyCalled;
        }
    }

    public static class PlainBean {
    }

    @Test
    public void testPostConstructIsCalled() throws BeansException {
        InitDestroyAnnotationBeanPostProcessor processor = new InitDestroyAnnotationBeanPostProcessor();
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);

        BeanDefinition bd = new BeanDefinition("testBean", PostConstructTestBean.class.getName());
        factory.registerBeanDefinition("testBean", bd);

        PostConstructTestBean bean = (PostConstructTestBean) factory.getBean("testBean");
        assertThat(bean.isPostConstructCalled()).isTrue();
    }

    @Test
    public void testPreDestroyIsCalledOnDestroy() throws BeansException {
        InitDestroyAnnotationBeanPostProcessor processor = new InitDestroyAnnotationBeanPostProcessor();
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);

        BeanDefinition bd = new BeanDefinition("testBean", PreDestroyTestBean.class.getName());
        factory.registerBeanDefinition("testBean", bd);

        PreDestroyTestBean bean = (PreDestroyTestBean) factory.getBean("testBean");
        assertThat(bean.isPreDestroyCalled()).isFalse();

        processor.destroy();
        assertThat(bean.isPreDestroyCalled()).isTrue();
    }

    @Test
    public void testPreDestroyNotCalledBeforeDestroyMethod() throws BeansException {
        InitDestroyAnnotationBeanPostProcessor processor = new InitDestroyAnnotationBeanPostProcessor();
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);

        BeanDefinition bd = new BeanDefinition("testBean", PreDestroyTestBean.class.getName());
        factory.registerBeanDefinition("testBean", bd);

        PreDestroyTestBean bean = (PreDestroyTestBean) factory.getBean("testBean");
        assertThat(bean.isPreDestroyCalled()).isFalse();
    }

    @Test
    public void testFullLifecycleOrder() throws BeansException {
        InitDestroyAnnotationBeanPostProcessor processor = new InitDestroyAnnotationBeanPostProcessor();
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);

        BeanDefinition bd = new BeanDefinition("lifecycleBean", FullLifecycleBean.class.getName());
        bd.setInitMethodName("customInit");
        factory.registerBeanDefinition("lifecycleBean", bd);

        FullLifecycleBean bean = (FullLifecycleBean) factory.getBean("lifecycleBean");
        assertThat(bean.isPostConstructCalled()).isTrue();
        assertThat(bean.isInitMethodCalled()).isTrue();
        assertThat(bean.isPreDestroyCalled()).isFalse();

        processor.destroy();
        assertThat(bean.isPreDestroyCalled()).isTrue();
    }

    @Test
    public void testPostConstructOnBeanWithoutAnnotation() throws BeansException {
        InitDestroyAnnotationBeanPostProcessor processor = new InitDestroyAnnotationBeanPostProcessor();
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);

        BeanDefinition bd = new BeanDefinition("plainBean", PlainBean.class.getName());
        factory.registerBeanDefinition("plainBean", bd);

        PlainBean bean = (PlainBean) factory.getBean("plainBean");
        assertThat(bean).isNotNull();
    }
}
