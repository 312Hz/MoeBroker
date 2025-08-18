package me.xiaoying.moebroker.api.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorManager {
    private static final Map<String, ExecutorService> executors = new HashMap<>();

    // 创建默认线程池
    static {
        ExecutorManager.getFixedExecutor("broker", Runtime.getRuntime().availableProcessors());
    }

    public static ExecutorService getExecutor(final String name) {
        return ExecutorManager.executors.get(name);
    }

    public static ExecutorService getFixedExecutor(String name, int size) {
        return ExecutorManager.executors.computeIfAbsent(name, k -> Executors.newFixedThreadPool(size));
    }

    public static ExecutorService getSingleExecutor(String name) {
        return ExecutorManager.executors.computeIfAbsent(name, k -> Executors.newSingleThreadExecutor());
    }

    public static ExecutorService getCacheExecutor(String name) {
        return ExecutorManager.executors.computeIfAbsent(name, k -> Executors.newCachedThreadPool());
    }

    public static ScheduledExecutorService getScheduledExecutor(String name, int size) {
        ExecutorService executor;

        if (ExecutorManager.executors.containsKey(name) && (executor = ExecutorManager.executors.get(name)) instanceof ScheduledExecutorService)
            return (ScheduledExecutorService) executor;

        executor = Executors.newScheduledThreadPool(size);
        ExecutorManager.executors.put(name, executor);
        return (ScheduledExecutorService) executor;
    }
}