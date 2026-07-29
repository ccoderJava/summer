package com.dianpoint.summer.test.event;

import com.dianpoint.summer.context.ApplicationEvent;
import com.dianpoint.summer.context.ApplicationListener;
import com.dianpoint.summer.context.SimpleApplicationEventPublisher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author wangyi
 * @date 2023/3/30
 */
public class SimpleApplicationEventPublisherTest {

    static SimpleApplicationEventPublisher simpleApplicationEventPublisher;

    @BeforeClass
    public static void setUp() {
        simpleApplicationEventPublisher = new SimpleApplicationEventPublisher();
        simpleApplicationEventPublisher.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
            }
        });
    }

    @Test
    public void testPublisher() throws Exception {
        simpleApplicationEventPublisher.publisher(new ApplicationEvent("event"));
    }

}