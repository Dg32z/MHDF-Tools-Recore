package cn.ChengZhiYa.MHDFTools.util.feature;

import cn.ChengZhiYa.MHDFTools.menu.fastuse.CustomMenu;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.FileUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

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
                .map(File::getName)
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

    /**
     * 为指定玩家实例打开自定义菜单
     *
     * @param player 玩家实例
     * @param id     菜单ID
     */
    public static void openCustomMenu(Player player, String id) {
        CustomMenu customMenu = new CustomMenu(player, id);
        customMenu.openMenu();
    }
}
