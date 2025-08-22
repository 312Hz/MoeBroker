package me.xiaoying.moebroker.server.bootstrap.api.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PluginDescription {
    private final String name;

    private final String version;

    private final String main;

    private final List<String> authors;

    private final String description;

    private final List<String> depend;

    private final List<String> sofDepend;

    public PluginDescription(final String name, final String version, final String main, final String description, final List<String> authors, final List<String> depend, final List<String> softDepend) {
        this.name = name;
        this.version = version;
        this.main = main;
        this.description = description;
        this.authors = authors;
        this.depend = depend;
        this.sofDepend = softDepend;
    }
}