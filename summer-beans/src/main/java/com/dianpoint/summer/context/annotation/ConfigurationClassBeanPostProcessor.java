package com.dianpoint.summer.context.annotation;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.BeanFactory;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.config.BeanPostProcessor;
import com.dianpoint.summer.beans.factory.support.BeanDefinitionRegistry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationClassBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(Configuration.class)) {
            processConfigurationClass(bean, clazz);
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    private void processConfigurationClass(Object configInstance, Class<?> clazz) {
        List<Method> beanMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Bean.class)) {
                beanMethods.add(method);
            }
        }

        for (Method method : beanMethods) {
            Bean beanAnnotation = method.getAnnotation(Bean.class);
            String beanName = beanAnnotation.name().isEmpty() ? method.getName() : beanAnnotation.name();
            String initMethodName = beanAnnotation.initMethod().isEmpty() ? null : beanAnnotation.initMethod();

            try {
                method.setAccessible(true);
                Object result = method.invoke(configInstance);

                BeanDefinition bd = new BeanDefinition(beanName, result.getClass().getName());
                if (initMethodName != null) {
                    bd.setInitMethodName(initMethodName);
                }

                BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
                registry.registerBeanDefinition(beanName, bd);
                if (beanFactory instanceof com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory) {
                    ((com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory) beanFactory)
                        .registerSingleton(beanName, result);
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
