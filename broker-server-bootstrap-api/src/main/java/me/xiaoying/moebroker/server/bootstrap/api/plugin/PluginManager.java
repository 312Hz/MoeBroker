package me.xiaoying.moebroker.server.bootstrap.api.plugin;

import java.io.File;

public interface PluginManager {
    Plugin getPlugin(String name);

    Plugin loadPlugin(File file);

    Plugin[] loadPlugins(File directory);

    Plugin[] getPlugins();

    void enablePlugin(Plugin plugin);

    void disablePlugin(Plugin plugin);
}