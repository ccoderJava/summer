package com.dianpoint.summer.test.component.scan;

import com.dianpoint.summer.stereotype.Component;

@Component
public class EchoService {

    public String echo(String message) {
        return message;
    }
}
