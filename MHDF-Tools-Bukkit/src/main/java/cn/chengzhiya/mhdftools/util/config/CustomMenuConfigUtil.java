package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.exception.FileException;
import cn.chengzhiya.mhdftools.exception.ResourceException;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public final class CustomMenuConfigUtil {
    @Getter
    private static final File customMenuFolder = new File(ConfigUtil.getDataFolder(), "customMenu");
    @Getter
    private static final HashMap<String, YamlConfiguration> customMenuHashMap = new HashMap<>();

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
     * 加载自定义菜单
     */
    public static void reloadMenu() {
        getCustomMenuHashMap().clear();
        for (File file : FileUtil.listFiles(getCustomMenuFolder())) {
            String path = file.getPath();
            if (!path.endsWith(".yml")) {
                return;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            getCustomMenuHashMap().put(path, config);
        }
    }

    /**
     * 获取自定义菜单列表
     *
     * @return 自定义菜单列表
     */
    public static Set<String> getCustomMenuList() {
        return getCustomMenuHashMap().keySet();
    }

    /**
     * 获取菜单配置文件实例
     *
     * @param id 菜单ID
     * @return 配置文件实例
     */
    public static YamlConfiguration getCustomMenuConfig(String id) {
        return getCustomMenuHashMap().get(id);
    }
}
