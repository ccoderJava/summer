package com.dianpoint.summer.web.servlet;

import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import com.dianpoint.summer.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private final Map<String, HandlerMethod> handlerMethods = new LinkedHashMap<>();

    public RequestMappingHandlerMapping(DefaultListableBeanFactory beanFactory) {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            try {
                Class<?> clazz = Class.forName(beanFactory.getBeanDefinition(beanName).getClassName());
                for (Method method : clazz.getDeclaredMethods()) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    if (mapping != null) {
                        String url = mapping.value();
                        if (url.isEmpty()) {
                            url = "/" + method.getName();
                        }
                        Object bean = beanFactory.getBean(beanName);
                        handlerMethods.put(url, new HandlerMethod(bean, method));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());
        return handlerMethods.get(path);
    }
}
