package com.dianpoint.summer.test.configuration;

import com.dianpoint.summer.context.annotation.Bean;
import com.dianpoint.summer.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyService();
    }

    @Bean(name = "namedBean")
    public String namedStringBean() {
        return "hello";
    }

    @Bean(initMethod = "trim")
    public String stringWithInitMethod() {
        return "  value  ";
    }
}
