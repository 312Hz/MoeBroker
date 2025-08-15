package me.xiaoying.moebroker.api.processor;

import me.xiaoying.moebroker.api.RemoteClient;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractProcessor<T> {
    public abstract void asyncRequest(RemoteClient remote, T t) throws Exception;

    public abstract Object syncRequest(RemoteClient remote, T t) throws Exception;

    @SuppressWarnings("unchecked")
    public Class<T> getBind() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();

        Type actualType = type.getActualTypeArguments()[0];

        try {
            return (Class<T>) this.getClass().getClassLoader().loadClass(actualType.getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}