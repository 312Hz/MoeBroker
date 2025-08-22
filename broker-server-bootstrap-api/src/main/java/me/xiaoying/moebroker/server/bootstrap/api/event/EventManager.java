package me.xiaoying.moebroker.server.bootstrap.api.event;

import me.xiaoying.moebroker.server.bootstrap.api.plugin.Plugin;

public interface EventManager {
    void registerEvent(Listener listener, Plugin plugin);

    void unregisterEvent(Listener listener, Plugin plugin);
}