package me.xiaoying.moebroker.api.processor;

import java.util.HashMap;
import java.util.Map;

public class ProcessorManager {
    private final Map<String, AbstractProcessor<?>> processors = new HashMap<>();

    public void registerProcessor(AbstractProcessor<?> abstractProcessor) {
        this.processors.put(abstractProcessor.getBind().getName(), abstractProcessor);
    }

    @SuppressWarnings("unchecked")
    public <T> AbstractProcessor<T> getProcessor(Class<T> clazz) {
        return (AbstractProcessor<T>) this.processors.get(clazz.getName());
    }

    public AbstractProcessor<?> getProcessor(String clazz) {
        return this.processors.get(clazz);
    }

    public void unregisterProcessor(Class<?> clazz) {
        this.processors.remove(clazz.getName());
    }

    public void unregisterProcessor(String clazz) {
        this.processors.remove(clazz);
    }
}