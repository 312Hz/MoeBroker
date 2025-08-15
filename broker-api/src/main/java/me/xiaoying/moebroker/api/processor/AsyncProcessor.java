package me.xiaoying.moebroker.api.processor;

import me.xiaoying.moebroker.api.RemoteClient;

public abstract class AsyncProcessor<T> extends AbstractProcessor<T> {
    @Override
    public Object syncRequest(RemoteClient remote, T t) throws Exception {
        throw new UnsupportedOperationException("SYNC handle unsupported in AsyncProcessor!");
    }
}