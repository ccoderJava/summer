package com.dianpoint.summer.core.scanner;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    public Set<Class<?>> scan(String basePackage) {
        Set<Class<?>> classes = new HashSet<>();
        String path = basePackage.replace('.', '/');
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource(path);
            if (resource == null) {
                return classes;
            }
            File directory = new File(resource.getFile());
            if (!directory.exists()) {
                return classes;
            }
            scanDirectory(directory, basePackage, classes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to scan package: " + basePackage, e);
        }
        return classes;
    }

    private void scanDirectory(File directory, String packageName, Set<Class<?>> classes) throws ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }
    }
}
