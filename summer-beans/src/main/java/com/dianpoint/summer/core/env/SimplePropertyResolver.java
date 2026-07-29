package com.dianpoint.summer.core.env;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimplePropertyResolver implements PropertyResolver {

    private final Map<String, String> properties;

    public SimplePropertyResolver() {
        this.properties = new ConcurrentHashMap<>();
    }

    public SimplePropertyResolver(Map<String, String> properties) {
        this.properties = new ConcurrentHashMap<>(properties);
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
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

    public String resolvePlaceholders(String text) {
        if (text == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            int start = text.indexOf("${", i);
            if (start == -1) {
                result.append(text.substring(i));
                break;
            }
            result.append(text, i, start);
            int end = text.indexOf("}", start + 2);
            if (end == -1) {
                result.append(text.substring(start));
                break;
            }
            String placeholder = text.substring(start + 2, end);
            int colonIdx = placeholder.indexOf(':');
            String key;
            String defaultValue;
            if (colonIdx != -1) {
                key = placeholder.substring(0, colonIdx);
                defaultValue = placeholder.substring(colonIdx + 1);
            } else {
                key = placeholder;
                defaultValue = null;
            }
            String resolved = getProperty(key, defaultValue);
            if (resolved != null) {
                result.append(resolved);
            }
            i = end + 1;
        }
        return result.toString();
    }
}
