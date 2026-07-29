package com.dianpoint.summer.test.component.scan;

import com.dianpoint.summer.stereotype.Component;

@Component
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name;
    }
}
