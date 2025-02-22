package cn.ChengZhiYa.MHDFTools.util.config;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class MenuConfigUtil {
    /**
     * 保存初始自定义菜单
     */
    public static void saveDefaultMenu() throws ResourceException, FileException {
        FileUtil.createFolder(getMenuFolder());
        if (ConfigUtil.getConfig().getBoolean("homeSettings.enable")) {
            FileUtil.saveResource("menu/home.yml", "menu/home.yml", false);
        }
    }

    /**
     * 获取菜单目录实例
     *
     * @return 目录实例
     */
    public static File getMenuFolder() {
        return new File(ConfigUtil.getDataFolder(), "menu");
    }

    /**
     * 获取菜单文件实例
     *
     * @param file 文件名称
     * @return 菜单文件实例
     */
    public static File getMenuFile(String file) {
        return new File(getMenuFolder(), file);
    }

    /**
     * 获取菜单配置文件实例
     *
     * @param file 文件名称
     * @return 配置文件实例
     */
    public static YamlConfiguration getMenuConfig(String file) {
        return YamlConfiguration.loadConfiguration(getMenuFile(file));
    }
}
