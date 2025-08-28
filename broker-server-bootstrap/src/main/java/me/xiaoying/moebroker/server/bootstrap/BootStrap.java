package me.xiaoying.moebroker.server.bootstrap;

import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.file.SimpleFileManager;
import me.xiaoying.moebroker.server.BrokerServer;
import me.xiaoying.moebroker.server.bootstrap.api.BCore;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.Plugin;
import me.xiaoying.moebroker.server.bootstrap.command.SimpleCommandManager;
import me.xiaoying.moebroker.server.bootstrap.file.FileConfig;
import me.xiaoying.moebroker.server.bootstrap.logger.LoggerListener;
import me.xiaoying.moebroker.server.bootstrap.plugin.SimplePluginManager;

import java.io.File;
import java.text.DecimalFormat;

public class BootStrap {
    /** 控制台 */
    private static Terminal terminal;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        // initialize
        BootStrap.initialize();

        BCore.setServer(new Server(new BrokerAddress(FileConfig.SERVER_HOST, FileConfig.SERVER_PORT)).onStart(() -> Broker.getLogger().info("Done({}s)! For help, type \"help\"", new DecimalFormat("0.000").format((double) (System.currentTimeMillis() - start) / 1000))));
        BCore.getServer().run();

        BootStrap.terminal = new Terminal();
        EventHandle.registerEvent(BootStrap.terminal);
        BootStrap.terminal.run();

        // plugins
        for (Plugin plugin : BCore.getPluginManager().loadPlugins(new File("./plugins")))
            BCore.getPluginManager().enablePlugin(plugin);
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

        BCore.setPluginManager(new SimplePluginManager());

        BCore.setCommandManager(new SimpleCommandManager());
    }

    public static void unInitialize() {

    }

    public static Terminal getTerminal() {
        return BootStrap.terminal;
    }
}