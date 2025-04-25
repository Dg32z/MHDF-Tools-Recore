package cn.chengzhiya.mhdftools.util.config.plugin;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class CmiConfigUtil {
    @Getter
    private static final File dataFolder = new File(ConfigUtil.getDataFolder().getParent(), "CMI");
    @Getter
    private static final File configFile = new File(dataFolder, "config.yml");
    @Getter
    private static final File settingsFolder = new File(dataFolder, "Settings");
    @Getter
    private static final File databaseInfoFile = new File(settingsFolder, "DataBaseInfo.yml");
    private static YamlConfiguration config;
    private static YamlConfiguration databaseInfo;

    /**
     * 重新加载配置文件
     */
    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * 重新加载数据库配置文件
     */
    public static void reloadDatabaseInfo() {
        databaseInfo = YamlConfiguration.loadConfiguration(databaseInfoFile);
    }

    /**
     * 获取配置文件实例
     *
     * @return 配置文件实例
     */
    public static YamlConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }

        return config;
    }

    /**
     * 获取数据库配置文件实例
     *
     * @return 数据库配置文件实例
     */
    public static YamlConfiguration getDatabaseInfoConfig() {
        if (config == null) {
            reloadDatabaseInfo();
        }

        return databaseInfo;
    }
}
