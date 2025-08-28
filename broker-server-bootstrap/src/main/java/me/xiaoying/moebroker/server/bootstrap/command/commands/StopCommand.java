package me.xiaoying.moebroker.server.bootstrap.command.commands;

import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.server.bootstrap.BootStrap;
import me.xiaoying.moebroker.server.bootstrap.api.BCore;
import me.xiaoying.moebroker.server.bootstrap.api.command.Command;
import me.xiaoying.moebroker.server.bootstrap.api.command.CommandSender;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.Plugin;
import me.xiaoying.moebroker.server.bootstrap.command.SimpleCommandManager;

import java.util.Collections;
import java.util.List;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "Stop the server", "/stop");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String head, String[] args) {
        // plugin
        for (Plugin plugin : BCore.getPluginManager().getPlugins())
            BCore.getPluginManager().disablePlugin(plugin);

        // command
//        BCore.getCommandManager().getCommands().forEach((string, cmd) -> ((SimpleCommandManager) BCore.getCommandManager()).unregisterCommand(string));

        BCore.getCommandManager().getCommands().clear();

        // terminal
        BootStrap.getTerminal().close();

        // server
        BCore.getServer().close();
        Broker.getLogger().info("Stopping server...");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args) {
        return Collections.emptyList();
    }
}