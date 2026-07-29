package com.dianpoint.summer.web.servlet;

import com.dianpoint.summer.web.annotation.RequestParam;
import com.dianpoint.summer.web.annotation.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Object bean = handlerMethod.getBean();

        Object[] args = resolveArguments(request, response, method);
        Object result = method.invoke(bean, args);

        if (result == null) {
            return;
        }

        ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
        if (responseBody != null || method.getDeclaringClass().getAnnotation(ResponseBody.class) != null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(result));
        } else if (result instanceof String) {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write((String) result);
        }
    }

    private Object[] resolveArguments(HttpServletRequest request, HttpServletResponse response, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Class<?> type = parameters[i].getType();
            if (type == HttpServletRequest.class) {
                args[i] = request;
            } else if (type == HttpServletResponse.class) {
                args[i] = response;
            } else {
                RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                if (requestParam != null) {
                    String paramName = requestParam.value().isEmpty() ? requestParam.name() : requestParam.value();
                    String paramValue = request.getParameter(paramName);
                    if (paramValue == null && !requestParam.defaultValue().isEmpty()) {
                        paramValue = requestParam.defaultValue();
                    }
                    args[i] = convertValue(paramValue, type);
                }
            }
        }
        return args;
    }

    private Object convertValue(String value, Class<?> type) {
        if (value == null) return null;
        if (type == String.class) return value;
        if (type == int.class || type == Integer.class) return Integer.valueOf(value);
        if (type == long.class || type == Long.class) return Long.valueOf(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.valueOf(value);
        return value;
    }
}
