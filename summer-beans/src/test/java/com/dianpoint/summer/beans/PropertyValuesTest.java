package com.dianpoint.summer.beans;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyValuesTest {

    @Test
    public void testInitiallyEmpty() {
        PropertyValues pvs = new PropertyValues();
        assertThat(pvs.isEmpty()).isTrue();
        assertThat(pvs.size()).isEqualTo(0);
    }

    @Test
    public void testAddPropertyValue_andGetList() {
        PropertyValues pvs = new PropertyValues();
        PropertyValue pv1 = new PropertyValue("String", "name", "hello", false);
        PropertyValue pv2 = new PropertyValue("int", "age", 42, false);
        pvs.addPropertyValue(pv1);
        pvs.addPropertyValue(pv2);

        assertThat(pvs.isEmpty()).isFalse();
        assertThat(pvs.size()).isEqualTo(2);
        assertThat(pvs.getPropertyValueList()).containsExactly(pv1, pv2);
    }
}
