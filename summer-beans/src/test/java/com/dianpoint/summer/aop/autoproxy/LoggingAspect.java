package com.dianpoint.summer.aop.autoproxy;

import com.dianpoint.summer.aop.annotation.Aspect;
import com.dianpoint.summer.aop.annotation.Before;

@Aspect
public class LoggingAspect {

    private boolean beforeCalled = false;

    @Before("create*")
    public void logBefore() {
        beforeCalled = true;
    }

    public boolean isBeforeCalled() {
        return beforeCalled;
    }
}
