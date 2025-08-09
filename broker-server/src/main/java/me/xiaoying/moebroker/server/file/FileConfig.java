package me.xiaoying.moebroker.server.file;

import me.xiaoying.moebroker.api.file.SFile;

public class FileConfig extends SFile {
    public FileConfig() {
        super("Config.yml");
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onDisable() {

    }
}