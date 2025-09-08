package me.xiaoying.moebroker.server.bootstrap.plugin;

import me.xiaoying.moebroker.server.bootstrap.api.event.*;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.JavaPlugin;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.JavaPluginLoader;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.Plugin;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.PluginManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

public class SimplePluginManager implements PluginManager {
    /** 插件加载器(Bukkit 考虑多种加载器，MoeBroker 只考虑加载 jar) */
    private final JavaPluginLoader loader = new JavaPluginLoader();

    /** 已加载插件 */
    private final Map<String, Plugin> plugins = new HashMap<>();

    /** 缓存 softdepend 和 depend 待加载插件 */
    private final Map<String, File> depend = new HashMap<>();
    private final Map<String, File> softDepend = new HashMap<>();

    /** event */
    private final Map<EventPriority, List<RegisteredListener>> knownListeners = new HashMap<>();

    @Override
    public Plugin getPlugin(String name) {
        return this.plugins.get(name);
    }

    @Override
    public Plugin loadPlugin(File file) {
        Plugin plugin = this.loader.loadPlugin(file);

        if (plugin == null)
            return null;

        this.plugins.put(((JavaPlugin) plugin).getName(), plugin);
        return plugin;
    }

    @Override
    public Plugin[] loadPlugins(File directory) {
        Plugin[] plugins = this.loader.loadPlugins(directory);

        for (Plugin plugin : plugins) {
            if (plugin == null)
                continue;

            this.plugins.put(((JavaPlugin) plugin).getName(), plugin);
        }

        return plugins;
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

    @Override
    public void registerEvent(Listener listener, Plugin plugin) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            EventHandler annotation;
            if ((annotation = method.getAnnotation(EventHandler.class)) == null)
                continue;

            if (method.getParameterCount() != 1)
                continue;

            if (!Event.class.isAssignableFrom(method.getParameterTypes()[0]))
                continue;

            List<RegisteredListener> list = this.knownListeners.get(annotation.priority());

            if (list == null)
                list = new ArrayList<>();

            list.add(new RegisteredListener(listener, annotation.priority(), method, plugin));
            this.knownListeners.put(annotation.priority(), list);
        }
    }

    @Override
    public void unregisterEvent(Plugin plugin) {
        this.knownListeners.values().forEach(list -> {
            Iterator<RegisteredListener> iterator = list.iterator();

            RegisteredListener registeredListener;
            while (iterator.hasNext() && (registeredListener = iterator.next()) != null) {
                if (registeredListener.getPlugin() != plugin)
                    continue;

                iterator.remove();
            }
        });
    }

    @Override
    public void unregisterEvent(Listener listener) {
        this.knownListeners.values().forEach(list -> {
            Iterator<RegisteredListener> iterator = list.iterator();

            RegisteredListener registeredListener;
            while (iterator.hasNext() && (registeredListener = iterator.next()) != null) {
                if (registeredListener.getListener() != listener)
                    continue;

                iterator.remove();
            }
        });
    }

    @Override
    public void callEvent(Event event) {
        this.knownListeners.values().forEach(list -> list.forEach(registeredListener -> {
            if (!registeredListener.useful(event))
                return;

            registeredListener.callEvent(event);
        }));
    }
}