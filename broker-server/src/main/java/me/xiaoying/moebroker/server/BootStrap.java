package me.xiaoying.moebroker.server;

import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.file.SimpleFileManager;
import me.xiaoying.moebroker.server.file.FileConfig;
import me.xiaoying.moebroker.server.logger.LoggerListener;

public class BootStrap {
    public static void main(String[] args) {
        // 日志管理器
        EventHandle.registerEvent(new LoggerListener().initialize());

        // 初始化
        Broker.setLogger(LoggerFactory.getLogger());

        Broker.setLogger(Broker.getLogger());
        Broker.getLogger().info("Initializing Broker Server...");

        Broker.getLogger().info("Initializing FileManager...");
        Broker.setFileManager(new SimpleFileManager());
        Broker.getFileManager().register(new FileConfig());
        Broker.getFileManager().loads();

        BrokerServer brokerServer = new BrokerServer(new BrokerAddress("0.0.0.0", 22332));
        brokerServer.run();
    }
}