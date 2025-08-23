package me.xiaoying.moebroker.server.bootstrap.plugin;

import me.xiaoying.moebroker.server.bootstrap.api.plugin.JavaPluginLoader;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.Plugin;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.PluginManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SimplePluginManager implements PluginManager {
    /** 插件加载器(Bukkit 考虑多种加载器，MoeBroker 只考虑加载 jar) */
    private final JavaPluginLoader loader = new JavaPluginLoader();

    /** 已加载插件 */
    private final Map<String, Plugin> plugins = new HashMap<>();

    /** 缓存 softdepend 和 depend 待加载插件 */
    private final Map<String, File> depend = new HashMap<>();
    private final Map<String, File> softDepend = new HashMap<>();

    @Override
    public Plugin getPlugin(String name) {
        return this.plugins.get(name);
    }

    @Override
    public Plugin loadPlugin(File file) {
        return this.loader.loadPlugin(file);
    }

    @Override
    public Plugin[] loadPlugins(File directory) {
        return this.loader.loadPlugins(directory);
    }

    @Override
    public Plugin[] getPlugins() {
        return this.plugins.values().toArray(new Plugin[0]);
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        this.loader.enablePlugin(plugin);
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        this.loader.disablePlugin(plugin);
    }
}