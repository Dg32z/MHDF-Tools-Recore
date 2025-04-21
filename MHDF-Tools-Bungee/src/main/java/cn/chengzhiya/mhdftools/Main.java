package cn.chengzhiya.mhdftools;

import cn.chengzhiya.mhdftools.listener.PluginMessage;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {
    public static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        getProxy().getPluginManager().registerListener(this, new PluginMessage());
        getProxy().registerChannel("BungeeCord");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }
}
