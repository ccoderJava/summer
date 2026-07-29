package com.dianpoint.summer.aop.autoproxy;

import static org.assertj.core.api.Assertions.assertThat;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import com.dianpoint.summer.aop.aspectj.AspectJAutoProxyBeanPostProcessor;
import org.junit.Test;

public class AutoProxyTest {

    @Test
    public void testAutoProxyCreatesProxy() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new AspectJAutoProxyBeanPostProcessor());

        factory.registerBeanDefinition("loggingAspect",
            new BeanDefinition("loggingAspect", LoggingAspect.class.getName()));
        factory.registerBeanDefinition("orderService",
            new BeanDefinition("orderService", OrderServiceImpl.class.getName()));

        OrderService orderService = (OrderService) factory.getBean("orderService");
        assertThat(orderService).isNotNull();

        String result = orderService.createOrder("book");
        assertThat(result).isEqualTo("Order created: book");
    }

    @Test
    public void testAutoProxyBeforeAdviceCalled() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new AspectJAutoProxyBeanPostProcessor());

        factory.registerBeanDefinition("loggingAspect",
            new BeanDefinition("loggingAspect", LoggingAspect.class.getName()));
        factory.registerBeanDefinition("orderService",
            new BeanDefinition("orderService", OrderServiceImpl.class.getName()));

        OrderService orderService = (OrderService) factory.getBean("orderService");
        orderService.createOrder("book");

        LoggingAspect aspect = (LoggingAspect) factory.getBean("loggingAspect");
        assertThat(aspect.isBeforeCalled()).isTrue();
    }

    @Test
    public void testAutoProxyNonMatchingMethodNotIntercepted() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new AspectJAutoProxyBeanPostProcessor());

        factory.registerBeanDefinition("loggingAspect",
            new BeanDefinition("loggingAspect", LoggingAspect.class.getName()));
        factory.registerBeanDefinition("orderService",
            new BeanDefinition("orderService", OrderServiceImpl.class.getName()));

        OrderService orderService = (OrderService) factory.getBean("orderService");
        String result = orderService.cancelOrder("book");
        assertThat(result).isEqualTo("Order cancelled: book");
    }

    @Test
    public void testNoAspectMeansNoProxy() throws BeansException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new AspectJAutoProxyBeanPostProcessor());

        factory.registerBeanDefinition("orderService",
            new BeanDefinition("orderService", OrderServiceImpl.class.getName()));

        OrderService orderService = (OrderService) factory.getBean("orderService");
        assertThat(orderService).isInstanceOf(OrderServiceImpl.class);
    }
}
