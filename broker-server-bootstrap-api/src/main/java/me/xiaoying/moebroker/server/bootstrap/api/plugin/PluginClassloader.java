package me.xiaoying.moebroker.server.bootstrap.api.plugin;

import lombok.Getter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PluginClassloader extends URLClassLoader {
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

    private final JavaPluginLoader loader;

    @Getter
    private final PluginDescription description;

    @Getter
    private final JavaPlugin plugin;

    public PluginClassloader(JavaPluginLoader loader, ClassLoader classLoader, File file, PluginDescription description) throws MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, classLoader);

        this.loader = loader;
        this.description = description;

        Class<?> mainClass;
        try {
             mainClass = Class.forName(description.getMain(), true, this);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            mainClass.asSubclass(JavaPlugin.class);
        } catch (ClassCastException e) {
            throw new ClassCastException("Main class " + description.getMain() + " dose not extends JavaPlugin");
        }

        try {
            this.plugin = (JavaPlugin) mainClass.newInstance();
            this.plugin.init(description, this);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    Set<String> getClasses() {
        return this.classes.keySet();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return this.findClass(name);
    }

    Class<?> findClass(String name, boolean global) throws ClassNotFoundException {
        if (name.startsWith("me.xiaoying.moebroker.server."))
            throw new ClassNotFoundException(name);

        Class<?> result = this.classes.get(name);
        if (result != null)
            return result;
        if (global)
            return this.loader.getClassByName(name);

        result = super.findClass(name);

        if (result != null) {
            this.classes.put(name, result);
            this.loader.setClass(name, result);
        }

        return result;
    }
}