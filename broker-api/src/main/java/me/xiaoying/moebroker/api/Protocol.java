package me.xiaoying.moebroker.api;

public interface Protocol {
    void oneway(Object object);

    default Object invokeSync(Object object) {
        return this.invokeSync(object, 3000);
    }

    Object invokeSync(Object object, long timeoutMillis);
}