package me.xiaoying.moebroker.server.bootstrap.api.command;

import me.xiaoying.moebroker.server.bootstrap.api.plugin.Plugin;

import java.util.Map;

public interface CommandManager {

    void registerCommand(Plugin plugin, Command command);

    /**
     * Register command
     *
     * @param plugin Plugin
     * @param command SCommand
     */
//    void registerCommand(Plugin plugin, SCommand command);

    /**
     * Get command by name or alias
     *
     * @param command Command's name or alias
     * @return Command
     */
    Command getCommand(String command);

    Map<String, Command> getCommands();

    void unregisterCommand(String command, Plugin plugin);

    boolean dispatch(CommandSender sender, String command);
}