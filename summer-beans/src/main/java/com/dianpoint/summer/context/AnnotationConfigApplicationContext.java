package com.dianpoint.summer.context;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.dianpoint.summer.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
import com.dianpoint.summer.beans.factory.config.BeanFactoryPostProcessor;
import com.dianpoint.summer.beans.factory.config.ConfigurableListableBeanFactory;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import com.dianpoint.summer.core.scanner.ClassPathComponentScanner;

import java.util.ArrayList;
import java.util.List;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    private List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    private InitDestroyAnnotationBeanPostProcessor initDestroyBeanPostProcessor;

    public AnnotationConfigApplicationContext(String... basePackages) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        ClassPathComponentScanner scanner = new ClassPathComponentScanner(factory);
        scanner.scan(basePackages);
        this.beanFactory = factory;
        try {
            refresh();
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DefaultListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    @Override
    public void registerBean(String beanName, Object object) {
        this.beanFactory.registerBean(beanName, object);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanFactory.isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanFactory.isPrototype(name);
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanFactory.getType(name);
    }

    @Override
    public void publisher(ApplicationEvent event) {
        this.getApplicationEventPublisher().publisher(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void finishRefresh() {
        publisher(new ContextRefreshEvent("Application context refreshed finish"));
    }

    @Override
    public void registerListeners() {
        this.getApplicationEventPublisher().addApplicationListener(new ApplicationListener());
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher applicationEventPublisher = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(applicationEventPublisher);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        for (BeanFactoryPostProcessor processor : this.getBeanFactoryPostProcessors()) {
            try {
                processor.postProcessBeanFactory(configurableListableBeanFactory);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
        this.initDestroyBeanPostProcessor = new InitDestroyAnnotationBeanPostProcessor();
        this.beanFactory.addBeanPostProcessor(this.initDestroyBeanPostProcessor);
    }

    @Override
    public void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    public void close() {
        if (this.initDestroyBeanPostProcessor != null) {
            this.initDestroyBeanPostProcessor.destroy();
        }
        super.close();
    }
}
