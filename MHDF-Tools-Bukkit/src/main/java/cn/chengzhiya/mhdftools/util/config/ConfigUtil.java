package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.exception.ResourceException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public final class ConfigUtil {
    /**
     * 获取插件数据文件夹
     */
    public static File getDataFolder() {
        return Main.instance.getDataFolder();
    }

    /**
     * 获取配置文件实例
     *
     * @return 配置文件实例
     */
    public static FileConfiguration getConfig() {
        return Main.instance.getConfig();
    }

    /**
     * 保存配置文件
     */
    public static void saveConfig() {
        Main.instance.saveConfig();
    }

    /**
     * 重新加载配置文件
     */
    public static void reloadConfig() {
        Main.instance.reloadConfig();
        System.out.println(getConfig().getKeys(true));
    }

    /**
     * 保存初始配置文件
     */
    public static void saveDefaultConfig() throws ResourceException {
        FileUtil.saveResource("config.yml", "config_zh.yml", false);
    }
}
