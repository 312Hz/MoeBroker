package me.xiaoying.moebroker.server.bootstrap;

import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.file.SimpleFileManager;
import me.xiaoying.moebroker.server.BrokerServer;
import me.xiaoying.moebroker.server.bootstrap.file.FileConfig;
import me.xiaoying.moebroker.server.bootstrap.logger.LoggerListener;

import java.text.DecimalFormat;

public class BootStrap {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        // 日志管理器
        EventHandle.registerEvent(new LoggerListener().initialize());

        // 初始化
        Broker.setLogger(LoggerFactory.getLogger());

        Broker.getLogger().info("Initializing Broker Server...");

        Broker.getLogger().info("Initializing FileManager...");
        Broker.setFileManager(new SimpleFileManager());
        Broker.getFileManager().register(new FileConfig());
        Broker.getFileManager().loads();

        BrokerServer server = new Server(new BrokerAddress("0.0.0.0", 22332)).onStart(() -> Broker.getLogger().info("Done({}s)! For help, type \"help\"", new DecimalFormat("0.000").format((double) (System.currentTimeMillis() - start) / 1000)));
        server.run();
    }
}