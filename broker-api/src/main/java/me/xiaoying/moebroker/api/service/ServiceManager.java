package me.xiaoying.moebroker.api.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {
    private static final Map<String, Object> services = new HashMap<>();

    public static <T> void registerService(Class<T> clazz, T service) {
        ServiceManager.services.put(clazz.getName(), service);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> clazz) {
        return (T) ServiceManager.services.get(clazz.getName());
    }
}