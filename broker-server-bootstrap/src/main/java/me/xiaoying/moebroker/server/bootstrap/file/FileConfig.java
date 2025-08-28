package me.xiaoying.moebroker.server.bootstrap.file;

import me.xiaoying.moebroker.api.file.SFile;

public class FileConfig extends SFile {
    public static String SERVER_HOST;
    public static int SERVER_PORT;

    public FileConfig() {
        super("Config.yml");
    }

    @Override
    public void onLoad() {
        FileConfig.SERVER_HOST = this.getConfiguration().getString("Server.Host");
        FileConfig.SERVER_PORT = this.getConfiguration().getInt("Server.Port");
    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onDisable() {

    }
}