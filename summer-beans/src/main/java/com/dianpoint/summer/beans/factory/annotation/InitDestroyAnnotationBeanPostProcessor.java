package com.dianpoint.summer.beans.factory.annotation;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.BeanFactory;
import com.dianpoint.summer.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InitDestroyAnnotationBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;
    private final Map<String, Object> preDestroyBeans = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                if (method.getParameterTypes().length != 0) {
                    throw new BeansException(
                        "@PostConstruct method " + method.getName() + " must have no parameters");
                }
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                } catch (IllegalAccessException e) {
                    throw new BeansException(
                        "Failed to invoke @PostConstruct method " + method.getName());
                } catch (InvocationTargetException e) {
                    throw new BeansException(
                        "@PostConstruct method " + method.getName() + " threw an exception");
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PreDestroy.class)) {
                if (method.getParameterTypes().length != 0) {
                    throw new BeansException(
                        "@PreDestroy method " + method.getName() + " must have no parameters");
                }
                preDestroyBeans.put(beanName, bean);
                break;
            }
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void destroy() {
        for (Map.Entry<String, Object> entry : preDestroyBeans.entrySet()) {
            Object bean = entry.getValue();
            Class<?> clazz = bean.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(PreDestroy.class)) {
                    try {
                        method.setAccessible(true);
                        method.invoke(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        preDestroyBeans.clear();
    }
}
