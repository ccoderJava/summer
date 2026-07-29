package com.dianpoint.summer.context;

import java.util.ArrayList;
import java.util.List;

public class SimpleApplicationEventPublisher implements ApplicationEventPublisher {

    private List<ApplicationListener<?>> listeners = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked")
    public void publisher(ApplicationEvent event) {
        for (ApplicationListener<?> listener : listeners) {
            try {
                ((ApplicationListener<ApplicationEvent>) listener).onApplicationEvent(event);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        this.listeners.add(listener);
    }
}
