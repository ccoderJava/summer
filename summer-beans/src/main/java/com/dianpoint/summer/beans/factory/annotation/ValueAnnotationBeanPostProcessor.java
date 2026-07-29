package com.dianpoint.summer.beans.factory.annotation;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.BeanFactory;
import com.dianpoint.summer.beans.factory.config.BeanPostProcessor;
import com.dianpoint.summer.core.env.PropertyResolver;
import com.dianpoint.summer.core.env.SimplePropertyResolver;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public class ValueAnnotationBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;
    private final SimplePropertyResolver propertyResolver = new SimplePropertyResolver(new ConcurrentHashMap<String, String>());

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                String expression = valueAnnotation.value();
                String resolved = propertyResolver.resolvePlaceholders(expression);
                if (resolved == null || resolved.equals(expression)) {
                    resolved = expression;
                }
                field.setAccessible(true);
                try {
                    if (field.getType() == int.class || field.getType() == Integer.class) {
                        field.set(bean, Integer.valueOf(resolved));
                    } else if (field.getType() == long.class || field.getType() == Long.class) {
                        field.set(bean, Long.valueOf(resolved));
                    } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        field.set(bean, Boolean.valueOf(resolved));
                    } else {
                        field.set(bean, resolved);
                    }
                } catch (IllegalAccessException e) {
                    throw new BeansException("Failed to set @Value field " + field.getName());
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }

    public void addProperty(String key, String value) {
        propertyResolver.addProperty(key, value);
    }
}
