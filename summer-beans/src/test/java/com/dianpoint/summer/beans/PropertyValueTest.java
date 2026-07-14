package com.dianpoint.summer.beans;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyValueTest {

    @Test
    public void testConstructor_andGetters() {
        PropertyValue pv = new PropertyValue("String", "name", "hello", false);
        assertThat(pv.getType()).isEqualTo("String");
        assertThat(pv.getName()).isEqualTo("name");
        assertThat(pv.getValue()).isEqualTo("hello");
        assertThat(pv.isRef()).isFalse();
    }

    @Test
    public void testIsRef_true() {
        PropertyValue pv = new PropertyValue("ref", "service", "userService", true);
        assertThat(pv.isRef()).isTrue();
    }
}
