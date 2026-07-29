package com.dianpoint.summer.test.beans;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.annotation.Value;
import com.dianpoint.summer.beans.factory.annotation.ValueAnnotationBeanPostProcessor;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueAnnotationTest {

    public static class ValueBean {
        @Value("${app.name}")
        private String appName;

        @Value("${app.version:1.0.0}")
        private String appVersion;

        @Value("${app.port:8080}")
        private int port;

        @Value("staticValue")
        private String staticField;

        public String getAppName() {
            return appName;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public int getPort() {
            return port;
        }

        public String getStaticField() {
            return staticField;
        }
    }

    @Test
    public void testValueInjectionFromProperty() throws BeansException {
        ValueAnnotationBeanPostProcessor processor = new ValueAnnotationBeanPostProcessor();
        processor.addProperty("app.name", "SummerApp");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);

        BeanDefinition bd = new BeanDefinition("valueBean", ValueBean.class.getName());
        factory.registerBeanDefinition("valueBean", bd);

        ValueBean bean = (ValueBean) factory.getBean("valueBean");
        assertThat(bean.getAppName()).isEqualTo("SummerApp");
    }

    @Test
    public void testValueInjectionWithDefaultValue() throws BeansException {
        ValueAnnotationBeanPostProcessor processor = new ValueAnnotationBeanPostProcessor();
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);

        BeanDefinition bd = new BeanDefinition("valueBean", ValueBean.class.getName());
        factory.registerBeanDefinition("valueBean", bd);

        ValueBean bean = (ValueBean) factory.getBean("valueBean");
        assertThat(bean.getAppVersion()).isEqualTo("1.0.0");
    }

    @Test
    public void testValueInjectionForIntType() throws BeansException {
        ValueAnnotationBeanPostProcessor processor = new ValueAnnotationBeanPostProcessor();
        processor.addProperty("app.port", "9090");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);

        BeanDefinition bd = new BeanDefinition("valueBean", ValueBean.class.getName());
        factory.registerBeanDefinition("valueBean", bd);

        ValueBean bean = (ValueBean) factory.getBean("valueBean");
        assertThat(bean.getPort()).isEqualTo(9090);
    }

    @Test
    public void testValueInjectionStaticValue() throws BeansException {
        ValueAnnotationBeanPostProcessor processor = new ValueAnnotationBeanPostProcessor();
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);

        BeanDefinition bd = new BeanDefinition("valueBean", ValueBean.class.getName());
        factory.registerBeanDefinition("valueBean", bd);

        ValueBean bean = (ValueBean) factory.getBean("valueBean");
        assertThat(bean.getStaticField()).isEqualTo("staticValue");
    }

    @Test
    public void testPropertyResolverContainsProperty() {
        ValueAnnotationBeanPostProcessor processor = new ValueAnnotationBeanPostProcessor();
        processor.addProperty("my.key", "myValue");
        assertThat(processor.getPropertyResolver().containsProperty("my.key")).isTrue();
        assertThat(processor.getPropertyResolver().containsProperty("nonexistent")).isFalse();
    }

    @Test
    public void testPropertyResolverGetPropertyWithDefault() {
        ValueAnnotationBeanPostProcessor processor = new ValueAnnotationBeanPostProcessor();
        processor.addProperty("my.key", "myValue");
        assertThat(processor.getPropertyResolver().getProperty("my.key", "default")).isEqualTo("myValue");
        assertThat(processor.getPropertyResolver().getProperty("nonexistent", "default")).isEqualTo("default");
    }
}
