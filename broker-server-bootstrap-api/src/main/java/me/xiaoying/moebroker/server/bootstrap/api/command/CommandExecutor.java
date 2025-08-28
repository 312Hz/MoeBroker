package me.xiaoying.moebroker.server.bootstrap.api.command;

import java.util.List;

public interface CommandExecutor {
    /**
     * Execute command
     *
     * @param sender Who called this command
     * @param args parameters
     * @return result
     */
    boolean onCommand(CommandSender sender, Command command, String head, String[] args);

    /**
     * Tab executor
     *
     * @param sender Who called this method
     * @param command Command
     * @param head command's head
     * @param args command's parameters
     * @return tab parameters
     */
    List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args);
}