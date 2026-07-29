package com.dianpoint.summer.core.scanner;

import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.BeanDefinitionRegistry;
import com.dianpoint.summer.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ClassPathComponentScanner {

    private final BeanDefinitionRegistry registry;
    private final ClassPathScanner scanner;

    public ClassPathComponentScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.scanner = new ClassPathScanner();
    }

    public int scan(String... basePackages) {
        int count = 0;
        for (String basePackage : basePackages) {
            Set<Class<?>> classes = scanner.scan(basePackage);
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(Component.class)) {
                    String beanName = resolveBeanName(clazz);
                    BeanDefinition bd = new BeanDefinition(beanName, clazz.getName());
                    registry.registerBeanDefinition(beanName, bd);
                    count++;
                }
            }
        }
        return count;
    }

    private String resolveBeanName(Class<?> clazz) {
        Component component = clazz.getAnnotation(Component.class);
        if (component != null && !component.value().isEmpty()) {
            return component.value();
        }
        String className = clazz.getSimpleName();
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}
