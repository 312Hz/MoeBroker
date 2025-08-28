package me.xiaoying.moebroker.server.bootstrap.api.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Command implements CommandExecutor {
    @Getter
    private final String name;

    @Getter
    private final String description;

    @Getter
    private final String usage;

    @Getter
    private final List<String> alias;

    public Command(String name, String description, String usage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.alias = new ArrayList<>();
    }

    public Command(String name, String description, String usage, List<String> alias) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.alias = alias;
    }
}