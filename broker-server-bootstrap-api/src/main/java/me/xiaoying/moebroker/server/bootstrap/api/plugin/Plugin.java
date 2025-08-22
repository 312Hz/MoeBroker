package me.xiaoying.moebroker.server.bootstrap.api.plugin;

/**
 * 插件基类<br>
 * 事实上有思路看出 Bukkit 想要支持多种类型插件，或是考虑到自定义插件，故提供了 PluginBase<br>
 * MoeBroker 不考虑自定义插件类型，甚至连 Plugin.class 都不该有，但是我懒得改了
 */
public abstract class Plugin {
    /** 插件是否开启 */
    private boolean enabled;

    public void onLoad() {}

    public void onEnable() {}

    public void onDisable() {}

    public boolean isEnabled() {
        return this.enabled;
    }

    protected final void setEnabled(boolean enabled) {
        if (this.enabled == enabled)
            return;

        this.enabled = enabled;

        if (this.enabled)
            this.onEnable();
        else
            this.onDisable();
    }
}