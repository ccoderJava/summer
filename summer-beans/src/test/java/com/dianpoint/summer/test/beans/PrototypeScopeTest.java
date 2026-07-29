package com.dianpoint.summer.test.beans;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PrototypeScopeTest {

    public static class TestBean {
    }

    public static class CounterBean {
        private static int counter = 0;
        private final int id;

        public CounterBean() {
            counter++;
            id = counter;
        }

        public int getId() {
            return id;
        }
    }

    @Test
    public void testPrototypeBeanCreatesNewInstance() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition bd = new BeanDefinition("protoBean", CounterBean.class.getName());
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        factory.registerBeanDefinition("protoBean", bd);

        CounterBean bean1 = (CounterBean) factory.getBean("protoBean");
        CounterBean bean2 = (CounterBean) factory.getBean("protoBean");

        assertThat(bean1).isNotNull();
        assertThat(bean2).isNotNull();
        assertThat(bean1).isNotSameAs(bean2);
        assertThat(bean1.getId()).isNotEqualTo(bean2.getId());
    }

    @Test
    public void testPrototypeBeanNotInSingletonRegistry() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition bd = new BeanDefinition("protoBean", TestBean.class.getName());
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        factory.registerBeanDefinition("protoBean", bd);

        factory.getBean("protoBean");

        assertThat(factory.containsSingleton("protoBean")).isFalse();
    }

    @Test
    public void testIsPrototypeReturnsTrue() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition bd = new BeanDefinition("protoBean", TestBean.class.getName());
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        factory.registerBeanDefinition("protoBean", bd);

        assertThat(factory.isPrototype("protoBean")).isTrue();
    }

    @Test
    public void testIsSingletonReturnsFalse() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition bd = new BeanDefinition("protoBean", TestBean.class.getName());
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        factory.registerBeanDefinition("protoBean", bd);

        assertThat(factory.isSingleton("protoBean")).isFalse();
    }

    @Test
    public void testRefreshSkipsPrototypeBean() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition protoBd = new BeanDefinition("protoBean", TestBean.class.getName());
        protoBd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        protoBd.setLazyInit(false);
        factory.registerBeanDefinition("protoBean", protoBd);

        BeanDefinition singBd = new BeanDefinition("singBean", TestBean.class.getName());
        singBd.setLazyInit(false);
        factory.registerBeanDefinition("singBean", singBd);

        assertThat(factory.containsSingleton("singBean")).isTrue();
        assertThat(factory.containsSingleton("protoBean")).isFalse();
    }

    @Test
    public void testSingletonBeanStillWorks() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition bd = new BeanDefinition("singBean", TestBean.class.getName());
        factory.registerBeanDefinition("singBean", bd);

        TestBean bean1 = (TestBean) factory.getBean("singBean");
        TestBean bean2 = (TestBean) factory.getBean("singBean");

        assertThat(bean1).isSameAs(bean2);
    }

    @Test
    public void testPrototypeBeanWithInitMethod() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition bd = new BeanDefinition("protoBean", InitMethodBean.class.getName());
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        bd.setInitMethodName("init");
        factory.registerBeanDefinition("protoBean", bd);

        InitMethodBean bean1 = (InitMethodBean) factory.getBean("protoBean");
        InitMethodBean bean2 = (InitMethodBean) factory.getBean("protoBean");

        assertThat(bean1.isInitialized()).isTrue();
        assertThat(bean2.isInitialized()).isTrue();
        assertThat(bean1).isNotSameAs(bean2);
    }

    public static class InitMethodBean {
        private boolean initialized = false;

        public void init() {
            initialized = true;
        }

        public boolean isInitialized() {
            return initialized;
        }
    }
}
