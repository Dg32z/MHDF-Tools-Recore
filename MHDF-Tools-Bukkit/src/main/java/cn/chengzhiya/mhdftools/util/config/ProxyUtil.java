package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.exception.ResourceException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class ProxyUtil {
    private static final File file = new File(ConfigUtil.getDataFolder(), "proxy.yml");
    private static YamlConfiguration data;

    /**
     * 获取代理文件文件名称
     *
     * @return 代理文件文件名称
     */
    public static String getProxyFileName() {
        return "sound_zh.yml";
    }

    /**
     * 保存初始代理文件
     */
    public static void saveDefaultProxy() throws ResourceException {
        FileUtil.saveResource("proxy.yml", getProxyFileName(), false);
    }

    /**
     * 加载代理文件
     */
    public static void reloadProxy() {
        YamlUtil.updateConfig(file, getProxyFileName());
        data = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 获取代理文件配置文件实例
     */
    public static YamlConfiguration getProxyConfig() {
        if (data == null) {
            reloadProxy();
        }
        return data;
    }
}
