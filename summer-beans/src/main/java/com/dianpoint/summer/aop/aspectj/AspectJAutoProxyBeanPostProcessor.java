package com.dianpoint.summer.aop.aspectj;

import com.dianpoint.summer.aop.Advisor;
import com.dianpoint.summer.aop.AfterReturningAdvice;
import com.dianpoint.summer.aop.AfterReturningAdviceInterceptor;
import com.dianpoint.summer.aop.AfterThrowingAdviceInterceptor;
import com.dianpoint.summer.aop.AroundAdvice;
import com.dianpoint.summer.aop.AroundAdviceInterceptor;
import com.dianpoint.summer.aop.DefaultAdvisor;
import com.dianpoint.summer.aop.JdkDynamicAopProxy;
import com.dianpoint.summer.aop.MethodBeforeAdvice;
import com.dianpoint.summer.aop.MethodBeforeAdviceInterceptor;
import com.dianpoint.summer.aop.MethodInterceptor;
import com.dianpoint.summer.aop.NameMatchMethodPointcut;
import com.dianpoint.summer.aop.Pointcut;
import com.dianpoint.summer.aop.ProceedingJoinPoint;
import com.dianpoint.summer.aop.ThrowsAdvice;
import com.dianpoint.summer.aop.annotation.After;
import com.dianpoint.summer.aop.annotation.AfterThrowing;
import com.dianpoint.summer.aop.annotation.Around;
import com.dianpoint.summer.aop.annotation.Aspect;
import com.dianpoint.summer.aop.annotation.Before;
import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.BeanFactory;
import com.dianpoint.summer.beans.factory.config.BeanPostProcessor;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AspectJAutoProxyBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;
    private final List<Advisor> advisors = new ArrayList<>();
    private volatile boolean aspectsProcessed = false;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!aspectsProcessed) {
            synchronized (this) {
                if (!aspectsProcessed) {
                    scanAspects();
                    aspectsProcessed = true;
                }
            }
        }

        return createProxyIfNeeded(bean, beanName);
    }

    private Object createProxyIfNeeded(Object bean, String beanName) {
        if (advisors.isEmpty()) {
            return bean;
        }

        Class<?> targetClass = bean.getClass();
        List<MethodInterceptor> matchingInterceptors = new ArrayList<>();

        for (Advisor advisor : advisors) {
            Pointcut pointcut = advisor.getPointcut();
            if (pointcut == null) {
                matchingInterceptors.addAll(advisor.getMethodInterceptors());
                continue;
            }
            for (Method method : targetClass.getMethods()) {
                if (pointcut.matches(method, targetClass)) {
                    matchingInterceptors.addAll(advisor.getMethodInterceptors());
                    break;
                }
            }
        }

        if (matchingInterceptors.isEmpty()) {
            return bean;
        }

        Class<?>[] interfaces = targetClass.getInterfaces();
        if (interfaces.length == 0) {
            return bean;
        }

        DefaultAdvisor combinedAdvisor = new DefaultAdvisor();
        for (MethodInterceptor interceptor : matchingInterceptors) {
            combinedAdvisor.addMethodInterceptor(interceptor);
        }

        JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(bean, combinedAdvisor);
        Object proxy = aopProxy.getProxy();

        if (beanFactory instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory lbf = (DefaultListableBeanFactory) beanFactory;
            if (lbf.containsSingleton(beanName)) {
                lbf.removeSingleton(beanName);
                lbf.registerSingleton(beanName, proxy);
            }
        }

        return proxy;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    private void scanAspects() {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
        for (String beanName : factory.getBeanDefinitionNames()) {
            try {
                Class<?> clazz = Class.forName(factory.getBeanDefinition(beanName).getClassName());
                if (!clazz.isAnnotationPresent(Aspect.class)) {
                    continue;
                }

                Object aspectInstance = factory.getBean(beanName);
                for (Method method : clazz.getDeclaredMethods()) {
                    processAdviceMethod(aspectInstance, method);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processAdviceMethod(Object aspectInstance, Method method) {
        Before before = method.getAnnotation(Before.class);
        After after = method.getAnnotation(After.class);
        Around around = method.getAnnotation(Around.class);
        AfterThrowing afterThrowing = method.getAnnotation(AfterThrowing.class);

        String expression = null;
        MethodInterceptor interceptor = null;

        if (before != null) {
            expression = before.value();
            interceptor = new MethodBeforeAdviceInterceptor(new MethodBeforeAdviceAdapter(aspectInstance, method));
        } else if (after != null) {
            expression = after.value();
            interceptor = new AfterReturningAdviceInterceptor(new AfterReturningAdviceAdapter(aspectInstance, method));
        } else if (around != null) {
            expression = around.value();
            interceptor = new AroundAdviceInterceptor(new AroundAdviceAdapter(aspectInstance, method));
        } else if (afterThrowing != null) {
            expression = afterThrowing.value();
            interceptor = new AfterThrowingAdviceInterceptor(new ThrowsAdviceAdapter(aspectInstance, method));
        }

        if (interceptor != null) {
            DefaultAdvisor advisor = new DefaultAdvisor();
            advisor.addMethodInterceptor(interceptor);
            if (expression != null && !expression.isEmpty()) {
                advisor.setPointcut(new NameMatchMethodPointcut(expression));
            }
            advisors.add(advisor);
        }
    }

    private static class MethodBeforeAdviceAdapter implements MethodBeforeAdvice {
        private final Object target;
        private final Method method;
        MethodBeforeAdviceAdapter(Object target, Method method) {
            this.target = target;
            this.method = method;
            this.method.setAccessible(true);
        }

        @Override
        public void before(Method m, Object[] args, Object obj) throws Throwable {
            method.invoke(target);
        }
    }

    private static class AfterReturningAdviceAdapter implements AfterReturningAdvice {
        private final Object target;
        private final Method method;
        AfterReturningAdviceAdapter(Object target, Method method) {
            this.target = target;
            this.method = method;
            this.method.setAccessible(true);
        }

        @Override
        public void afterReturning(Object returnValue, Method m, Object[] args, Object obj) throws Throwable {
            method.invoke(target);
        }
    }

    private static class AroundAdviceAdapter implements AroundAdvice {
        private final Object target;
        private final Method method;
        AroundAdviceAdapter(Object target, Method method) {
            this.target = target;
            this.method = method;
            this.method.setAccessible(true);
        }

        @Override
        public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
            return method.invoke(target, joinPoint);
        }
    }

    private static class ThrowsAdviceAdapter implements ThrowsAdvice {
        private final Object target;
        private final Method method;
        ThrowsAdviceAdapter(Object target, Method method) {
            this.target = target;
            this.method = method;
            this.method.setAccessible(true);
        }

        @Override
        public void afterThrowing(Method m, Object[] args, Object obj, Throwable exception) throws Throwable {
            method.invoke(target);
        }
    }
}
