package me.xiaoying.moebroker.api.processor;

import me.xiaoying.moebroker.api.RemoteClient;

public abstract class SyncProcessor<T> extends AbstractProcessor<T> {
    @Override
    public void asyncRequest(RemoteClient remote, T t) throws Exception {
        throw new UnsupportedOperationException("ASYNC handle unsupported in SyncProcessor!");
    }
}