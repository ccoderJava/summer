package com.dianpoint.summer.test.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import com.dianpoint.summer.web.servlet.HandlerMethod;
import com.dianpoint.summer.web.servlet.RequestMappingHandlerAdapter;
import com.dianpoint.summer.web.servlet.RequestMappingHandlerMapping;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;

public class RequestMappingHandlerMappingTest {

    private DefaultListableBeanFactory beanFactory;
    private RequestMappingHandlerMapping handlerMapping;
    private RequestMappingHandlerAdapter handlerAdapter;

    @Before
    public void setUp() {
        beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("helloController",
            new BeanDefinition("helloController", HelloController.class.getName()));

        handlerMapping = new RequestMappingHandlerMapping(beanFactory);
        handlerAdapter = new RequestMappingHandlerAdapter();
    }

    @Test
    public void testHandlerMappingResolvesUrl() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest("/hello", "GET");
        HandlerMethod handler = handlerMapping.getHandler(request);
        assertThat(handler).isNotNull();
        assertThat(handler.getBean()).isInstanceOf(HelloController.class);
        assertThat(handler.getMethod().getName()).isEqualTo("hello");
    }

    @Test
    public void testHandlerMappingReturnsNullForUnknownUrl() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest("/unknown", "GET");
        HandlerMethod handler = handlerMapping.getHandler(request);
        assertThat(handler).isNull();
    }

    @Test
    public void testHandlerAdapterInvokesMethod() throws Exception {
        helloControllerInit();

        MockHttpServletRequest request = new MockHttpServletRequest("/hello", "GET");
        request.setParameter("name", "World");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerMethod handler = handlerMapping.getHandler(request);
        handlerAdapter.handle(request, response, handler);

        assertThat(response.getContentAsString()).isEqualTo("Hello, World");
    }

    @Test
    public void testHandlerAdapterReturnsJson() throws Exception {
        helloControllerInit();

        MockHttpServletRequest request = new MockHttpServletRequest("/json", "GET");
        request.setParameter("id", "42");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerMethod handler = handlerMapping.getHandler(request);
        handlerAdapter.handle(request, response, handler);

        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("\"id\":42");
        assertThat(response.getContentAsString()).contains("\"name\":\"Test\"");
    }

    @Test
    public void testHandlerAdapterDefaultParameterValue() throws Exception {
        helloControllerInit();

        MockHttpServletRequest request = new MockHttpServletRequest("/json", "GET");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerMethod handler = handlerMapping.getHandler(request);
        handlerAdapter.handle(request, response, handler);

        assertThat(response.getContentAsString()).contains("\"id\":0");
    }

    @Test
    public void testHandlerAdapterSupports() {
        assertThat(handlerAdapter.supports(new HandlerMethod(new HelloController(),
            HelloController.class.getDeclaredMethods()[0]))).isTrue();
    }

    private void helloControllerInit() throws BeansException {
        beanFactory.getBean("helloController");
    }
}
