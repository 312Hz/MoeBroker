package me.xiaoying.moebroker.server.bootstrap.api;

import me.xiaoying.moebroker.server.bootstrap.api.plugin.JavaPlugin;

import java.util.Locale;

public class NamespacedKey {
    private final String space;
    private final String value;

    public NamespacedKey(JavaPlugin plugin, String value) {
        this.space = plugin.getName();
        this.value = value;
    }

    @Override
    public String toString() {
        return this.space.toLowerCase(Locale.ENGLISH) + ":" + this.value.toLowerCase(Locale.ENGLISH);
    }
}