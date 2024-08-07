package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.task.player.*;
import cn.ChengZhiYa.MHDFTools.task.server.ServerScoreboardTask;
import cn.ChengZhiYa.MHDFTools.task.server.ServerTimeMessageTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class AsyncTask {
    FileConfiguration config;
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    public void start() {
        config = MHDFTools.instance.getConfig();

        asyncCommand(new ServerScoreboardTask(), "ScoreboardSettings.Enable");
        asyncCommand(new PlayerLoginTask(), "LoginSystemSettings.Enable");
        asyncCommand(new PlayerFlightTask(), "FlySettings.Enable");
        asyncCommand(new PlayerBackTask(), "BackSettings.Enable");
        asyncCommand(new PlayerTpBackTask(), "TpBackSettings.Enable");
        asyncCommand(new PlayerVanishTask(), "VanishSettings.Enable");
        asyncCommand(new PlayerTpaTask(), "TpaSettings.Enable");
        asyncCommand(new PlayerTpaHereTask(), "TpaHereSettings.Enable");

        if (config.getBoolean("TimeMessageSettings.Enable")) {
            int delay = config.getInt("TimeMessageSettings.Delay"); //这个是例外 (:
            new ServerTimeMessageTask().runTaskTimerAsynchronously(plugin, 0L, delay * 20L);
        }
    }

    private void asyncCommand(Runnable task, String configKey) {
        if (config.getBoolean(configKey)) {
            ((BukkitRunnable) task).runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
    }
}