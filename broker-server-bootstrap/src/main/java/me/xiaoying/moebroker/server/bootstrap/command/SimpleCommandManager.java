package me.xiaoying.moebroker.server.bootstrap.command;

import me.xiaoying.moebroker.server.bootstrap.api.NamespacedKey;
import me.xiaoying.moebroker.server.bootstrap.api.command.Command;
import me.xiaoying.moebroker.server.bootstrap.api.command.CommandManager;
import me.xiaoying.moebroker.server.bootstrap.api.command.CommandSender;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.JavaPlugin;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.Plugin;
import me.xiaoying.moebroker.server.bootstrap.command.commands.HelpCommand;
import me.xiaoying.moebroker.server.bootstrap.command.commands.StopCommand;

import java.util.*;

public class SimpleCommandManager implements CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public SimpleCommandManager() {
        this.registerCommand(new HelpCommand());
        this.registerCommand(new StopCommand());
    }

    public void registerCommand(Command command) {
        this.commands.put("moebroker:" + command.getName(), command);
    }

    @Override
    public void registerCommand(Plugin plugin, Command command) {
        this.commands.put(new NamespacedKey((JavaPlugin) plugin, command.getName()).toString(), command);

        if (command.getAlias() == null || command.getAlias().isEmpty())
            return;

        for (String alias : command.getAlias())
            this.commands.put(new NamespacedKey((JavaPlugin) plugin, alias).toString(), command);
    }

    @Override
    public Command getCommand(String command) {
        return this.commands.get(this.matchCommand(command));
    }

    @Override
    public Map<String, Command> getCommands() {
        return this.commands;
    }

    public void unregisterCommand(String command) {
        if (command == null || command.isEmpty())
            return;

        if (!command.contains(":"))
            command = "moebroker:" + command;

        this.commands.remove(command);
    }

    @Override
    public void unregisterCommand(String command, Plugin plugin) {
        Iterator<String> iterator = this.commands.keySet().iterator();

        String string;

        while (iterator.hasNext() && (string = iterator.next()) != null) {
            if (!string.equalsIgnoreCase(new NamespacedKey((JavaPlugin) plugin, command).toString()))
                continue;

            iterator.remove();
        }
    }

    @Override
    public boolean dispatch(CommandSender sender, String command) {
        String[] split = command.split(" ");
        String head = split[0];

        head = this.matchCommand(head);
        Command cmd = this.getCommand(head);

        if (cmd == null)
            return false;

        String[] parameters = {};
        if (split.length != 1)
            parameters = new ArrayList<>(Arrays.asList(split)).subList(1, split.length).toArray(new String[0]);

        try {
            cmd.onCommand(sender, cmd, head, parameters);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private String matchCommand(String command) {
        if (command == null || command.contains(":"))
            return command;

        String prefix = null;
        for (String s : this.commands.keySet()) {
            if (!s.endsWith(":" + command))
                continue;

            prefix = s.replace(":" + command, "");
        }

        if (prefix == null)
            prefix = "moebroker";
        return prefix + ":" + command;
    }
}