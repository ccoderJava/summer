package com.dianpoint.summer.test.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import com.dianpoint.summer.context.annotation.ConfigurationClassBeanPostProcessor;
import org.junit.Test;

public class ConfigurationBeanTest {

    @Test
    public void testConfigurationClassCreatesBeanDefinitions() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new ConfigurationClassBeanPostProcessor());

        BeanDefinition bd = new BeanDefinition("appConfig", AppConfig.class.getName());
        bd.setLazyInit(false);
        factory.registerBeanDefinition("appConfig", bd);

        MyService myService = (MyService) factory.getBean("myService");
        assertThat(myService).isNotNull();
        assertThat(myService.getName()).isEqualTo("default");
    }

    @Test
    public void testConfigurationBeanWithCustomName() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new ConfigurationClassBeanPostProcessor());

        BeanDefinition bd = new BeanDefinition("appConfig", AppConfig.class.getName());
        bd.setLazyInit(false);
        factory.registerBeanDefinition("appConfig", bd);

        String namedBean = (String) factory.getBean("namedBean");
        assertThat(namedBean).isEqualTo("hello");
    }

    @Test
    public void testConfigurationBeanWithInitMethod() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new ConfigurationClassBeanPostProcessor());

        BeanDefinition bd = new BeanDefinition("appConfig", AppConfig.class.getName());
        bd.setLazyInit(false);
        factory.registerBeanDefinition("appConfig", bd);

        String result = (String) factory.getBean("stringWithInitMethod");
        assertThat(result).isNotNull();
    }

    @Test
    public void testConfigurationClassIsAlsoRegistered() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new ConfigurationClassBeanPostProcessor());

        BeanDefinition bd = new BeanDefinition("appConfig", AppConfig.class.getName());
        bd.setLazyInit(false);
        factory.registerBeanDefinition("appConfig", bd);

        AppConfig config = (AppConfig) factory.getBean("appConfig");
        assertThat(config).isNotNull();
    }

    @Test
    public void testNoConfigurationAnnotationDoesNothing() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new ConfigurationClassBeanPostProcessor());

        BeanDefinition bd = new BeanDefinition("myService", MyService.class.getName());
        bd.setLazyInit(false);
        factory.registerBeanDefinition("myService", bd);

        MyService service = (MyService) factory.getBean("myService");
        assertThat(service).isNotNull();
    }
}
