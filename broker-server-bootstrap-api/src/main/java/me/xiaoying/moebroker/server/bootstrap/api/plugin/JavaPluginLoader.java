package me.xiaoying.moebroker.server.bootstrap.api.plugin;

import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.configuration.YamlConfiguration;
import me.xiaoying.moebroker.api.utils.ZipUtil;
import me.xiaoying.moebroker.server.bootstrap.api.utils.YamlUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JavaPluginLoader {
    /** softdepend 和 depend 等待列表 */
    private final Map<String, File> prepareLoad = new HashMap<>();

    private final Map<String, Class<?>> classes = new HashMap<>();

    /** 已加载 Plugin 的 PluginClassLoader */
    private final Map<String, PluginClassloader> loaders = new HashMap<>();

    /**
     * 加载单个 Plugin 文件
     *
     * @param file File(需以 .jar 结尾，并且是 JarFile 可读的(ZipFile)压缩文件)
     */
    public Plugin loadPlugin(File file) {
        if (!file.exists())
            return null;

        // Bukkit 可能考虑到支持多种 Plugin 接口，所以使用了 registerInterface
        // MoeBroker 只考虑加载 jar，故直接判断文件名称和文件类型

        if (!file.getName().endsWith(".jar"))
            return null;

        try {
            JarFile jar = new JarFile(file);

            ZipEntry entry = jar.getEntry("plugin.yml");
            if (entry == null)
                Broker.getLogger().warn("No plugin.yml found in " + file.getName());

            // PluginDescription
            String content = ZipUtil.getFile(file.getPath(), "plugin.yml");

            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(content);
            String name = configuration.getString("name");
            String version = configuration.getString("version");
            String main = configuration.getString("main");
            String description = configuration.getString("description");

            if (name == null)
                throw new NullPointerException("Missing name option in plugin.yml of " + file.getName());
            if (version == null)
                throw new NullPointerException("Missing version option in plugin.yml of " + file.getName());
            if (main == null)
                throw new NullPointerException("Missing main option in plugin.yml of " + file.getName());

            List<Object> nodes = YamlUtil.getNodes(content);

            List<String> authors = new ArrayList<>();
            if (nodes.contains("author"))
                authors.add(configuration.getString("author"));
            if (nodes.contains("authors"))
                authors.addAll(configuration.getStringList("authors"));

            List<String> dependencies = new ArrayList<>();
            List<String> softDependencies = new ArrayList<>();

            if (nodes.contains("depend"))
                dependencies.addAll(configuration.getStringList("depend"));
            if (nodes.contains("softDepend"))
                softDependencies.addAll(configuration.getStringList("softDepend"));

            PluginDescription pluginDescription = new PluginDescription(name, version, main, description, authors, dependencies, softDependencies);

            PluginClassloader pluginClassloader = new PluginClassloader(this, this.getClass().getClassLoader(), file, pluginDescription);
            this.loaders.put(name, pluginClassloader);

            jar.close();
            return pluginClassloader.getPlugin();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载目录中的 Plugin
     *
     * @param dataFolder File(文件夹)
     */
    public Plugin[] loadPlugins(File dataFolder) {
        if (dataFolder == null)
            return new Plugin[0];

        if (!dataFolder.exists())
            dataFolder.mkdirs();

        if (!dataFolder.isDirectory())
            return new Plugin[] { this.loadPlugin(dataFolder) };

        List<Plugin> list = new ArrayList<>();
        for (File file : Objects.requireNonNull(dataFolder.listFiles()))
            list.add(this.loadPlugin(file));

        return list.toArray(new Plugin[0]);
    }

    public void enablePlugin(Plugin plugin) {
        if (plugin.isEnabled())
            Broker.getLogger().warn("Plugin {} already enabled.", ((JavaPlugin) plugin).getDescription().getName());

        JavaPlugin jPlugin = (JavaPlugin) plugin;
        List<String> authors = jPlugin.getDescription().getAuthors();

        StringBuilder stringBuilder  = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            stringBuilder.append(authors.get(i));

            if (i == authors.size() - 1)
                break;

            stringBuilder.append(", ");
        }

        Broker.getLogger().info("Enabling {} {} by {}", jPlugin.getName(), jPlugin.getVersion(), stringBuilder.toString());

        if (!this.loaders.containsKey(jPlugin.getDescription().getName()))
            this.loaders.put(jPlugin.getDescription().getName(), (PluginClassloader) jPlugin.getClassLoader());

        plugin.setEnabled(true);
    }

    public void disablePlugin(Plugin plugin) {
        if (plugin.isEnabled())
            Broker.getLogger().warn("Plugin {} already disabled.", ((JavaPlugin) plugin).getDescription().getName());

        JavaPlugin jPlugin = (JavaPlugin) plugin;

        Broker.getLogger().info("Disabling {}", jPlugin.getName());

        try {
            jPlugin.setEnabled(false);
        } catch (Throwable ex) {
            Broker.getLogger().warn("Error occurred while disabling {} (Is it up to date?)\n{}", jPlugin.getName(), ex.getMessage());
        }

        this.loaders.remove(jPlugin.getDescription().getName());

        if (!(jPlugin.getClassLoader() instanceof PluginClassloader))
            return;

        PluginClassloader loader = (PluginClassloader) jPlugin.getClassLoader();

        for (String clazz : loader.getClasses())
            this.removeClass(clazz);
    }

    Class<?> getClassByName(String name) {
        Class<?> clazz = this.classes.get(name);

        if (clazz != null)
            return clazz;

        for (String string : this.loaders.keySet()) {
            PluginClassloader pluginClassloader = this.loaders.get(string);

            try {
                clazz = pluginClassloader.findClass(name, false);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if (clazz != null)
                return clazz;
        }

        return null;
    }

    void setClass(String name, Class<?> clazz) {
        if (this.classes.containsKey(name))
            return;

        this.classes.put(name, clazz);
    }

    private void removeClass(String name) {
        this.classes.remove(name);
    }
}