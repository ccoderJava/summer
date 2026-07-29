package com.dianpoint.summer.test.beans;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.BeanFactory;
import com.dianpoint.summer.beans.factory.config.BeanFactoryPostProcessor;
import com.dianpoint.summer.beans.factory.config.ConfigurableListableBeanFactory;
import com.dianpoint.summer.context.ClassPathXmlApplicationContext;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanFactoryPostProcessorTest {

    @Test
    public void testBeanFactoryPostProcessorIsInvoked() throws BeansException {
        AtomicBoolean invoked = new AtomicBoolean(false);
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/beanAutowire.xml", false);
        ctx.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException {
                invoked.set(true);
            }
        });
        ctx.refresh();
        assertThat(invoked.get()).isTrue();
    }

    @Test
    public void testBeanFactoryPostProcessorAccessesFactory() throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/beanAutowire.xml", false);
        ctx.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException {
                ConfigurableListableBeanFactory clbf = (ConfigurableListableBeanFactory) beanFactory;
                assertThat(clbf.getBeanDefinitionCount()).isGreaterThan(0);
            }
        });
        ctx.refresh();
        assertThat(ctx.getBeanDefinitionCount()).isGreaterThan(0);
    }

    @Test
    public void testMultipleBeanFactoryPostProcessors() throws BeansException {
        AtomicBoolean firstInvoked = new AtomicBoolean(false);
        AtomicBoolean secondInvoked = new AtomicBoolean(false);
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/beanAutowire.xml", false);
        ctx.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException {
                firstInvoked.set(true);
            }
        });
        ctx.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException {
                secondInvoked.set(true);
            }
        });
        ctx.refresh();
        assertThat(firstInvoked.get()).isTrue();
        assertThat(secondInvoked.get()).isTrue();
    }
}
