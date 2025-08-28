package me.xiaoying.moebroker.server.bootstrap.api;

import me.xiaoying.moebroker.server.BrokerServer;
import me.xiaoying.moebroker.server.bootstrap.api.command.CommandManager;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.PluginManager;

public class BCore {
    /** Broker服务端 */
    private static BrokerServer server;

    /** 命令管理器 */
    private static CommandManager commandManager;

    /** 插件管理器 */
    private static PluginManager pluginManager;

    public static BrokerServer getServer() {
        return BCore.server;
    }

    public static void setServer(final BrokerServer server) {
        if (BCore.server != null)
            return;

        BCore.server = server;
    }

    public static CommandManager getCommandManager() {
        return BCore.commandManager;
    }

    public static void setCommandManager(CommandManager commandManager) {
        if (BCore.commandManager != null)
            return;

        BCore.commandManager = commandManager;
    }

    public static PluginManager getPluginManager() {
        return BCore.pluginManager;
    }

    public static void setPluginManager(PluginManager pluginManager) {
        if (BCore.pluginManager != null)
            return;

        BCore.pluginManager = pluginManager;
    }
}