package com.dianpoint.summer.test.event;

import com.dianpoint.summer.context.ApplicationEvent;
import com.dianpoint.summer.context.ApplicationListener;
import com.dianpoint.summer.context.ContextRefreshEvent;
import com.dianpoint.summer.context.SimpleApplicationEventPublisher;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationListenerInterfaceTest {

    @Test
    public void testApplicationListenerIsInterface() {
        ApplicationListener<ApplicationEvent> listener = new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
            }
        };
        assertThat(listener).isNotNull();
        assertThat(listener instanceof ApplicationListener).isTrue();
    }

    @Test
    public void testApplicationListenerReceivesEvents() {
        AtomicInteger count = new AtomicInteger(0);
        SimpleApplicationEventPublisher publisher = new SimpleApplicationEventPublisher();
        publisher.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                count.incrementAndGet();
            }
        });
        publisher.publisher(new ApplicationEvent("test"));
        assertThat(count.get()).isEqualTo(1);
    }

    @Test
    public void testApplicationListenerReceivesMultipleEvents() {
        AtomicInteger count = new AtomicInteger(0);
        SimpleApplicationEventPublisher publisher = new SimpleApplicationEventPublisher();
        publisher.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                count.incrementAndGet();
            }
        });
        publisher.publisher(new ApplicationEvent("event1"));
        publisher.publisher(new ApplicationEvent("event2"));
        publisher.publisher(new ContextRefreshEvent("context refreshed"));
        assertThat(count.get()).isEqualTo(3);
    }

    @Test
    public void testMultipleListenersReceiveSameEvent() {
        AtomicInteger firstCount = new AtomicInteger(0);
        AtomicInteger secondCount = new AtomicInteger(0);
        SimpleApplicationEventPublisher publisher = new SimpleApplicationEventPublisher();
        publisher.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                firstCount.incrementAndGet();
            }
        });
        publisher.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                secondCount.incrementAndGet();
            }
        });
        publisher.publisher(new ApplicationEvent("test"));
        assertThat(firstCount.get()).isEqualTo(1);
        assertThat(secondCount.get()).isEqualTo(1);
    }
}
