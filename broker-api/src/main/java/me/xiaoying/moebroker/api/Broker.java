package me.xiaoying.moebroker.api;

import me.xiaoying.logger.Logger;
import me.xiaoying.moebroker.api.file.FileManager;

public class Broker {
    private static Logger logger;

    private static FileManager fileManager;

    /**
     * 获取日志对象
     *
     * @return Logger
     */
    public static Logger getLogger() {
        return Broker.logger;
    }

    /**
     * 设置日志对象
     *
     * @param logger Logger
     */
    public static void setLogger(Logger logger) {
        if (Broker.logger != null)
            return;

        Broker.logger = logger;
    }

    public static FileManager getFileManager() {
        return Broker.fileManager;
    }

    public static void setFileManager(FileManager fileManager) {
        if (Broker.fileManager != null)
            return;

        Broker.fileManager = fileManager;
    }
}