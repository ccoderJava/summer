package com.dianpoint.summer.test.env;

import static org.assertj.core.api.Assertions.assertThat;

import com.dianpoint.summer.core.env.StandardEnvironment;
import org.junit.Test;

public class StandardEnvironmentTest {

    @Test
    public void testContainsSystemProperty() {
        StandardEnvironment env = new StandardEnvironment();
        assertThat(env.containsProperty("java.version")).isTrue();
        assertThat(env.containsProperty("nonexistent.key.xyz")).isFalse();
    }

    @Test
    public void testGetSystemProperty() {
        StandardEnvironment env = new StandardEnvironment();
        assertThat(env.getProperty("java.version")).isNotNull();
    }

    @Test
    public void testGetPropertyWithDefault() {
        StandardEnvironment env = new StandardEnvironment();
        String value = env.getProperty("nonexistent.key.xyz", "default");
        assertThat(value).isEqualTo("default");
    }

    @Test
    public void testGetActiveProperties() {
        StandardEnvironment env = new StandardEnvironment();
        String[] props = env.getActiveProperties();
        assertThat(props.length).isGreaterThan(0);
    }
}
