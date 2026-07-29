package com.dianpoint.summer.beans.factory.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorArgumentValueTest {

    @Test
    public void testConstructor_twoArgs() {
        ConstructorArgumentValue cav = new ConstructorArgumentValue("String", "hello");
        assertThat(cav.getType()).isEqualTo("String");
        assertThat(cav.getValue()).isEqualTo("hello");
        assertThat(cav.getName()).isNull();
    }

    @Test
    public void testConstructor_threeArgs() {
        ConstructorArgumentValue cav = new ConstructorArgumentValue("int", "age", 42);
        assertThat(cav.getType()).isEqualTo("int");
        assertThat(cav.getName()).isEqualTo("age");
        assertThat(cav.getValue()).isEqualTo(42);
    }

    @Test
    public void testSetValue() {
        ConstructorArgumentValue cav = new ConstructorArgumentValue("String", "old");
        cav.setValue("new");
        assertThat(cav.getValue()).isEqualTo("new");
    }

    @Test
    public void testSetName() {
        ConstructorArgumentValue cav = new ConstructorArgumentValue("String", "hello");
        cav.setName("myName");
        assertThat(cav.getName()).isEqualTo("myName");
    }

    @Test
    public void testSetType() {
        ConstructorArgumentValue cav = new ConstructorArgumentValue("String", "hello");
        cav.setType("int");
        assertThat(cav.getType()).isEqualTo("int");
    }
}
