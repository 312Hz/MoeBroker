package me.xiaoying.moebroker.server.bootstrap.api.plugin;

import java.io.File;

public class JavaPlugin extends Plugin {
    private ClassLoader classloader;

    private PluginDescription description;

    public String getName() {
        return this.description.getName();
    }

    public String getVersion() {
        return this.description.getVersion();
    }

    public PluginDescription getDescription() {
        return this.description;
    }

    public ClassLoader getClassLoader() {
        return this.classloader;
    }

    public File getDataFolder() {
        return new File(System.getProperty("user.dir") + "/plugins/" + this.getName());
    }

    void init(PluginDescription description, ClassLoader classloader) {
        this.description = description;

        if (!(classloader instanceof PluginClassloader))
            throw new IllegalArgumentException("classloader must be an instance of PluginClassloader");

        this.classloader = classloader;
    }
}