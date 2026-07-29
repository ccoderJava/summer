package com.dianpoint.summer.core.env;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class StandardEnvironment implements Environment {

    private final Map<String, String> properties = new ConcurrentHashMap<>();

    public StandardEnvironment() {
        Properties sysProps = System.getProperties();
        for (String key : sysProps.stringPropertyNames()) {
            properties.put(key, sysProps.getProperty(key));
        }
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.containsKey(key);
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String value = properties.get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public String[] getActiveProperties() {
        return properties.keySet().toArray(new String[0]);
    }

    @Override
    public String[] getDefaultProperties() {
        return new String[0];
    }

    @Override
    public boolean acceptsProperties(String... profiles) {
        return true;
    }
}
