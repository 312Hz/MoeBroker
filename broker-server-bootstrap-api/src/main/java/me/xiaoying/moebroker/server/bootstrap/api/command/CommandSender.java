package me.xiaoying.moebroker.server.bootstrap.api.command;

public interface CommandSender {
    String getName();

    void sendMessage(String message);
}