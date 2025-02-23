package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.exception.FileException;
import cn.chengzhiya.mhdftools.exception.ResourceException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public final class CustomMenuConfigUtil {
    /**
     * 保存初始自定义菜单
     */
    public static void saveDefaultCustomMenu() throws ResourceException, FileException {
        if (!ConfigUtil.getConfig().getBoolean("customMenuSettings.enable")) {
            return;
        }
        if (getCustomMenuFolder().exists()) {
            return;
        }

        FileUtil.createFolder(getCustomMenuFolder());
        FileUtil.saveResource("customMenu/example.yml", "customMenu/example.yml", false);
    }

    /**
     * 获取自定义菜单目录实例
     *
     * @return 目录实例
     */
    public static File getCustomMenuFolder() {
        return new File(ConfigUtil.getDataFolder(), "customMenu");
    }

    /**
     * 获取自定义菜单文件实例列表
     *
     * @return 自定义菜单文件实例列表
     */
    public static List<File> getCustomMenuFileList() {
        return FileUtil.listFiles(getCustomMenuFolder());
    }

    /**
     * 获取自定义菜单配置实例列表
     *
     * @return 自定义菜单配置实例列表
     */
    public static List<YamlConfiguration> getCustomMenuConfigList() {
        return getCustomMenuFileList().stream()
                .map(YamlConfiguration::loadConfiguration)
                .toList();
    }

    /**
     * 获取自定义菜单列表
     *
     * @return 自定义菜单列表
     */
    public static List<String> getCustomMenuList() {
        return getCustomMenuFileList().stream()
                .map(File::getPath)
                .map(s -> s.replace("\\", "/"))
                .filter(s -> s.endsWith(".yml"))
                .map(s -> s.split("customMenu/")[1])
                .map(s -> s.replace(".yml", ""))
                .toList();
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
