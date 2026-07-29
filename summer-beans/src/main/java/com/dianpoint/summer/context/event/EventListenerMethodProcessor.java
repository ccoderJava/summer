package com.dianpoint.summer.context.event;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.BeanFactory;
import com.dianpoint.summer.beans.factory.config.BeanPostProcessor;
import com.dianpoint.summer.context.ApplicationEvent;
import com.dianpoint.summer.context.ApplicationListener;
import com.dianpoint.summer.context.SimpleApplicationEventPublisher;

import java.lang.reflect.Method;

public class EventListenerMethodProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;
    private final SimpleApplicationEventPublisher publisher = new SimpleApplicationEventPublisher();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            EventListener annotation = method.getAnnotation(EventListener.class);
            if (annotation != null) {
                registerListener(bean, method);
            }
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public SimpleApplicationEventPublisher getPublisher() {
        return publisher;
    }

    private void registerListener(Object bean, Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Class<?> eventType = paramTypes.length > 0 && ApplicationEvent.class.isAssignableFrom(paramTypes[0])
            ? paramTypes[0] : ApplicationEvent.class;

        ApplicationListener listener = new ApplicationListener() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                if (eventType.isInstance(event)) {
                    try {
                        method.setAccessible(true);
                        if (paramTypes.length == 1) {
                            method.invoke(bean, event);
                        } else {
                            method.invoke(bean);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        publisher.addApplicationListener(listener);
    }
}
