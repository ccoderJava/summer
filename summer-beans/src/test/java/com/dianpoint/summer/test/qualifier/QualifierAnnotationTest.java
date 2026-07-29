package com.dianpoint.summer.test.qualifier;

import static org.assertj.core.api.Assertions.assertThat;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

public class QualifierAnnotationTest {

    @Test
    public void testQualifierInjectsByQualifierValue() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition("databaseService",
            new BeanDefinition("databaseService", DatabaseService.class.getName()));
        factory.registerBeanDefinition("userService",
            new BeanDefinition("userService", UserService.class.getName()));

        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(factory);

        QualifierConsumer consumer = new QualifierConsumer();
        processor.postProcessBeforeInitialization(consumer, "consumer");

        assertThat(consumer.getDbService()).isNotNull();
        assertThat(consumer.getDbService().getType()).isEqualTo("default");
    }

    @Test
    public void testAutowiredWithoutQualifierUsesFieldName() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition("databaseService",
            new BeanDefinition("databaseService", DatabaseService.class.getName()));
        factory.registerBeanDefinition("userService",
            new BeanDefinition("userService", UserService.class.getName()));

        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(factory);

        QualifierConsumer consumer = new QualifierConsumer();
        processor.postProcessBeforeInitialization(consumer, "consumer");

        assertThat(consumer.getUserService()).isNotNull();
        assertThat(consumer.getUserService().getName()).isEqualTo("userService");
    }

    @Test
    public void testQualifierWithDifferentFieldName() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition("databaseService",
            new BeanDefinition("databaseService", DatabaseService.class.getName()));
        factory.registerBeanDefinition("userService",
            new BeanDefinition("userService", UserService.class.getName()));

        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(factory);

        QualifierConsumer consumer = new QualifierConsumer();
        processor.postProcessBeforeInitialization(consumer, "consumer");

        assertThat(consumer.getDbService()).isNotNull();
    }
}
