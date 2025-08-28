package me.xiaoying.moebroker.server.bootstrap.command;

import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.server.bootstrap.api.command.CommandSender;

public class ConsoleSender implements CommandSender {
    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void sendMessage(String message) {
        Broker.getLogger().info(message);
    }
}