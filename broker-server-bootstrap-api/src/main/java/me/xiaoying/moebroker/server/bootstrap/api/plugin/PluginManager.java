package me.xiaoying.moebroker.server.bootstrap.api.plugin;

import me.xiaoying.moebroker.server.bootstrap.api.event.Event;
import me.xiaoying.moebroker.server.bootstrap.api.event.Listener;

import java.io.File;

public interface PluginManager {
    Plugin getPlugin(String name);

    Plugin loadPlugin(File file);

    Plugin[] loadPlugins(File directory);

    Plugin[] getPlugins();

    void enablePlugin(Plugin plugin);

    void disablePlugin(Plugin plugin);

    void registerEvent(Listener listener, Plugin plugin);

    void unregisterEvent(Plugin plugin);

    void unregisterEvent(Listener listener);

    void callEvent(Event event);
}