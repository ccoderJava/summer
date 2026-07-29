package com.dianpoint.summer.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassUtilsTest {

    @Test
    public void testGetDefaultClassLoader_returnsNonNull() {
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        assertThat(cl).isNotNull();
    }

    @Test
    public void testGetDefaultClassLoader_canLoadClass() throws ClassNotFoundException {
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        assertThat(cl.loadClass("java.lang.String")).isEqualTo(String.class);
    }
}
