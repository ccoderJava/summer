package com.dianpoint.summer.context;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationEventTest {

    @Test
    public void testConstructor_setsSource() {
        Object source = "eventSource";
        ApplicationEvent event = new ApplicationEvent(source);
        assertThat(event.getSource()).isEqualTo(source);
    }

    @Test
    public void testMessage_equalsSourceToString() {
        Object source = "eventSource";
        ApplicationEvent event = new ApplicationEvent(source);
        assertThat(event.toString()).contains("eventSource");
    }
}
