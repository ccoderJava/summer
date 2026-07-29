package com.dianpoint.summer.beans.factory;

public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
