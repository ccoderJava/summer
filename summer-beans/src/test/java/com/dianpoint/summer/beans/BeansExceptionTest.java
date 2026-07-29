package com.dianpoint.summer.beans;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BeansExceptionTest {

    @Test
    public void testConstructor_withMessage() {
        BeansException ex = new BeansException("bean not found");
        assertThat(ex.getMessage()).isEqualTo("bean not found");
        assertThat(ex).isInstanceOf(Exception.class);
    }
}
