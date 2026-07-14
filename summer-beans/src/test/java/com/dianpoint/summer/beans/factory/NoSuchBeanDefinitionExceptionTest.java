package com.dianpoint.summer.beans.factory;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NoSuchBeanDefinitionExceptionTest {

    @Test
    public void testDefaultConstructor() {
        NoSuchBeanDefinitionException ex = new NoSuchBeanDefinitionException();
        assertThat(ex).isInstanceOf(Exception.class);
        assertThat(ex.getMessage()).isNull();
    }
}
