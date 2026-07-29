package com.dianpoint.summer.aop;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultAdvisorTest {

    @Test
    public void testGetMethodInterceptor_initiallyNull() {
        DefaultAdvisor advisor = new DefaultAdvisor();
        assertThat(advisor.getMethodInterceptor()).isNull();
    }

    @Test
    public void testSetAndGetMethodInterceptor() {
        DefaultAdvisor advisor = new DefaultAdvisor();
        MethodInterceptor interceptor = invocation -> "intercepted";
        advisor.setMethodInterceptor(interceptor);
        assertThat(advisor.getMethodInterceptor()).isSameAs(interceptor);
    }

    @Test
    public void testGetAdvice_returnsInterceptor() {
        DefaultAdvisor advisor = new DefaultAdvisor();
        MethodInterceptor interceptor = invocation -> "result";
        advisor.setMethodInterceptor(interceptor);
        assertThat(advisor.getAdvice()).isSameAs(interceptor);
    }

    @Test
    public void testGetMethodInterceptors_returnsList() {
        DefaultAdvisor advisor = new DefaultAdvisor();
        assertThat(advisor.getMethodInterceptors()).isNotNull();
        assertThat(advisor.getMethodInterceptors()).isEmpty();
    }

    @Test
    public void testAddMethodInterceptor() {
        DefaultAdvisor advisor = new DefaultAdvisor();
        MethodInterceptor interceptor = invocation -> "result";
        advisor.addMethodInterceptor(interceptor);
        assertThat(advisor.getMethodInterceptors()).hasSize(1);
        assertThat(advisor.getMethodInterceptor()).isSameAs(interceptor);
    }
}
