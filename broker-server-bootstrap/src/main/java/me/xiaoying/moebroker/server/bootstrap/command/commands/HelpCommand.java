package me.xiaoying.moebroker.server.bootstrap.command.commands;

import me.xiaoying.moebroker.server.bootstrap.api.command.Command;
import me.xiaoying.moebroker.server.bootstrap.api.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "help message", "/help");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String head, String[] args) {
        System.out.println(1231312);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args) {
        List<String> list = new ArrayList<>();

        list.add("hello");
        list.add("world");

        return list;
    }
}