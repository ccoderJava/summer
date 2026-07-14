package com.dianpoint.summer.context;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextRefreshEventTest {

    @Test
    public void testConstructor() {
        Object source = "refreshSource";
        ContextRefreshEvent event = new ContextRefreshEvent(source);
        assertThat(event.getSource()).isEqualTo(source);
        assertThat(event.toString()).isEqualTo("refreshSource");
    }
}
