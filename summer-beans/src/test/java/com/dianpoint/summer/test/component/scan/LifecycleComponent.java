package com.dianpoint.summer.test.component.scan;

import com.dianpoint.summer.stereotype.Component;
import com.dianpoint.summer.beans.factory.annotation.PostConstruct;
import com.dianpoint.summer.beans.factory.annotation.PreDestroy;

@Component
public class LifecycleComponent {

    private boolean postConstructCalled = false;
    private boolean preDestroyCalled = false;

    @PostConstruct
    public void init() {
        postConstructCalled = true;
    }

    @PreDestroy
    public void cleanup() {
        preDestroyCalled = true;
    }

    public boolean isPostConstructCalled() {
        return postConstructCalled;
    }

    public boolean isPreDestroyCalled() {
        return preDestroyCalled;
    }
}
