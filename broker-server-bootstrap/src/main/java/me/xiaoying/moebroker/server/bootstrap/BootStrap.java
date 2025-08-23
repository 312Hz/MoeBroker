package me.xiaoying.moebroker.server.bootstrap;

import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.file.SimpleFileManager;
import me.xiaoying.moebroker.server.BrokerServer;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.Plugin;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.PluginManager;
import me.xiaoying.moebroker.server.bootstrap.file.FileConfig;
import me.xiaoying.moebroker.server.bootstrap.logger.LoggerListener;
import me.xiaoying.moebroker.server.bootstrap.plugin.SimplePluginManager;

import java.io.File;
import java.text.DecimalFormat;

public class BootStrap {
    /** Broker 服务器 */
    private static BrokerServer server;

    /** 插件管理器 */
    private static PluginManager pluginManager;

    /** 控制台 */
    private static Terminal terminal;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        // initialize
        BootStrap.initialize();

        BootStrap.server = new Server(new BrokerAddress("0.0.0.0", 22332)).onStart(() -> Broker.getLogger().info("Done({}s)! For help, type \"help\"", new DecimalFormat("0.000").format((double) (System.currentTimeMillis() - start) / 1000)));
        BootStrap.server.run();

        BootStrap.terminal = new Terminal();
        EventHandle.registerEvent(BootStrap.terminal);
        BootStrap.terminal.run();

        // plugins
        for (Plugin plugin : BootStrap.pluginManager.loadPlugins(new File("./plugins")))
            BootStrap.pluginManager.enablePlugin(plugin);
    }

    public static void initialize() {
        // 日志管理器
        EventHandle.registerEvent(new LoggerListener().initialize());

        // 初始化
        Broker.setLogger(LoggerFactory.getLogger());

        Broker.getLogger().info("Initializing Broker Server...");

        Broker.getLogger().info("Initializing FileManager...");
        Broker.setFileManager(new SimpleFileManager());
        Broker.getFileManager().register(new FileConfig());
        Broker.getFileManager().loads();

        BootStrap.pluginManager = new SimplePluginManager();
    }

    public static void unInitialize() {

    }

    public static BrokerServer getServer() {
        return BootStrap.server;
    }

    public static Terminal getTerminal() {
        return BootStrap.terminal;
    }

    public static PluginManager getPluginManager() {
        return BootStrap.pluginManager;
    }
}