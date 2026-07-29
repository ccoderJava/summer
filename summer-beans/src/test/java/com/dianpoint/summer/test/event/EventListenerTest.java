package com.dianpoint.summer.test.event;

import com.dianpoint.summer.beans.BeansException;
import com.dianpoint.summer.beans.factory.config.BeanDefinition;
import com.dianpoint.summer.beans.factory.support.DefaultListableBeanFactory;
import com.dianpoint.summer.context.ApplicationEvent;
import com.dianpoint.summer.context.ContextRefreshEvent;
import com.dianpoint.summer.context.event.EventListener;
import com.dianpoint.summer.context.event.EventListenerMethodProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class EventListenerTest {

    private EventListenerMethodProcessor processor;
    private DefaultListableBeanFactory factory;

    public static class EventConsumer {
        private final AtomicInteger count = new AtomicInteger(0);

        @EventListener
        public void onEvent(ApplicationEvent event) {
            count.incrementAndGet();
        }

        public int getCount() {
            return count.get();
        }
    }

    @Before
    public void setUp() {
        processor = new EventListenerMethodProcessor();
        factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(processor);
        factory.registerBeanDefinition("consumer",
            new BeanDefinition("consumer", EventConsumer.class.getName()));
    }

    @Test
    public void testEventListenerReceivesEvent() throws BeansException {
        EventConsumer consumer = (EventConsumer) factory.getBean("consumer");
        processor.getPublisher().publisher(new ApplicationEvent("test"));
        assertThat(consumer.getCount()).isEqualTo(1);
    }

    @Test
    public void testEventListenerReceivesSubclassEvent() throws BeansException {
        EventConsumer consumer = (EventConsumer) factory.getBean("consumer");
        processor.getPublisher().publisher(new ContextRefreshEvent("refreshed"));
        assertThat(consumer.getCount()).isEqualTo(1);
    }

    @Test
    public void testEventListenerMultipleEvents() throws BeansException {
        EventConsumer consumer = (EventConsumer) factory.getBean("consumer");
        processor.getPublisher().publisher(new ApplicationEvent("first"));
        processor.getPublisher().publisher(new ApplicationEvent("second"));
        assertThat(consumer.getCount()).isEqualTo(2);
    }
}
