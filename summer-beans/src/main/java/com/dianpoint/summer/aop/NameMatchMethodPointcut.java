package com.dianpoint.summer.aop;

import java.lang.reflect.Method;

public class NameMatchMethodPointcut implements Pointcut {

    private String mappedName;

    public NameMatchMethodPointcut() {
    }

    public NameMatchMethodPointcut(String mappedName) {
        this.mappedName = mappedName;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    public String getMappedName() {
        return mappedName;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (mappedName == null || mappedName.isEmpty()) {
            return true;
        }
        return isMatch(method.getName(), mappedName);
    }

    private boolean isMatch(String methodName, String pattern) {
        if (pattern.equals("*")) {
            return true;
        }
        if (pattern.startsWith("*")) {
            return methodName.endsWith(pattern.substring(1));
        }
        if (pattern.endsWith("*")) {
            return methodName.startsWith(pattern.substring(0, pattern.length() - 1));
        }
        if (pattern.contains("*")) {
            int starIdx = pattern.indexOf('*');
            String prefix = pattern.substring(0, starIdx);
            String suffix = pattern.substring(starIdx + 1);
            return methodName.startsWith(prefix) && methodName.endsWith(suffix);
        }
        return methodName.equals(pattern);
    }
}
