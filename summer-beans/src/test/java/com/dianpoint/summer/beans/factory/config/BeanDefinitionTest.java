package com.dianpoint.summer.beans.factory.config;

import com.dianpoint.summer.beans.PropertyValue;
import com.dianpoint.summer.beans.PropertyValues;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanDefinitionTest {

    @Test
    public void testConstructor() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        assertThat(bd.getId()).isEqualTo("myBean");
        assertThat(bd.getClassName()).isEqualTo("com.example.MyBean");
    }

    @Test
    public void testDefaultSingleton() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        assertThat(bd.isSingleton()).isTrue();
        assertThat(bd.isPrototype()).isFalse();
    }

    @Test
    public void testSetScope_prototype() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        bd.setScope("prototype");
        assertThat(bd.isSingleton()).isFalse();
        assertThat(bd.isPrototype()).isTrue();
    }

    @Test
    public void testLazyInit_defaultTrue() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        assertThat(bd.isLazyInit()).isTrue();
    }

    @Test
    public void testSetLazyInit() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        bd.setLazyInit(false);
        assertThat(bd.isLazyInit()).isFalse();
    }

    @Test
    public void testSetAndGetDependsOn() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        bd.setDependsOn("dep1", "dep2");
        assertThat(bd.getDependsOn()).containsExactly("dep1", "dep2");
    }

    @Test
    public void testSetAndGetBeanClass() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        bd.setBeanClass(String.class);
        assertThat(bd.getBeanClass()).isEqualTo(String.class);
    }

    @Test
    public void testSetAndGetPropertyValues() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        PropertyValues pvs = new PropertyValues();
        pvs.addPropertyValue(new PropertyValue("String", "name", "hello", false));
        bd.setPropertyValues(pvs);
        assertThat(bd.getPropertyValues()).isSameAs(pvs);
    }

    @Test
    public void testSetAndGetConstructorArgumentValues() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        ConstructorArgumentValues cav = new ConstructorArgumentValues();
        cav.addArgumentValues(new ConstructorArgumentValue("String", "hello"));
        bd.setConstructorArgumentValues(cav);
        assertThat(bd.getConstructorArgumentValues()).isSameAs(cav);
    }

    @Test
    public void testSetAndGetInitMethodName() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        bd.setInitMethodName("init");
        assertThat(bd.getInitMethodName()).isEqualTo("init");
    }

    @Test
    public void testSetId() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        bd.setId("newId");
        assertThat(bd.getId()).isEqualTo("newId");
    }

    @Test
    public void testSetClassName() {
        BeanDefinition bd = new BeanDefinition("myBean", "com.example.MyBean");
        bd.setClassName("com.example.OtherBean");
        assertThat(bd.getClassName()).isEqualTo("com.example.OtherBean");
    }
}
