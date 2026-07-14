package com.dianpoint.summer.beans.factory.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorArgumentValuesTest {

    @Test
    public void testInitiallyEmpty() {
        ConstructorArgumentValues cav = new ConstructorArgumentValues();
        assertThat(cav.isEmpty()).isTrue();
        assertThat(cav.getArgumentCount()).isEqualTo(0);
    }

    @Test
    public void testAddAndGetByIndex() {
        ConstructorArgumentValues cav = new ConstructorArgumentValues();
        ConstructorArgumentValue v1 = new ConstructorArgumentValue("String", "hello");
        ConstructorArgumentValue v2 = new ConstructorArgumentValue("int", 42);
        cav.addArgumentValues(v1);
        cav.addArgumentValues(v2);

        assertThat(cav.isEmpty()).isFalse();
        assertThat(cav.getArgumentCount()).isEqualTo(2);
        assertThat(cav.getIndexedArgumentValue(0)).isSameAs(v1);
        assertThat(cav.getIndexedArgumentValue(1)).isSameAs(v2);
    }
}
