package cn.ChengZhiYa.MHDFTools.util.feature;

import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class CustomMenuUtil {
    /**
     * 获取自定义菜单目录实例
     *
     * @return 目录实例
     */
    public static File getCustomMenuFolder() {
        return new File(ConfigUtil.getDataFolder(), "customMenu");
    }

    /**
     * 获取自定义菜单文件实例
     *
     * @param file 文件名称
     * @return 菜单文件实例
     */
    public static File getCustomMenuFile(String file) {
        return new File(getCustomMenuFolder(), file);
    }

    /**
     * 获取自定义菜单配置文件实例
     *
     * @param file 文件名称
     * @return 配置文件实例
     */
    public static YamlConfiguration getCustomMenuConfig(String file) {
        return YamlConfiguration.loadConfiguration(getCustomMenuFile(file));
    }
}
