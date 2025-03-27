package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class YamlUtil {
    /**
     * 获取配置项列表
     *
     * @param config 配置实例
     * @param key    目标key
     * @return 配置项列表
     */
    public static List<YamlConfiguration> getConfigurationSectionList(ConfigurationSection config, String key) {
        List<?> data = config.getList(key);
        if (data == null) {
            return new ArrayList<>();
        }

        Yaml yaml = new Yaml();

        return data.stream()
                .map(yaml::dump)
                .map(StringReader::new)
                .map(YamlConfiguration::loadConfiguration)
                .toList();
    }

    /**
     * 更新配置文件
     *
     * @param configFile        配置文件文件实例
     * @param jarConfigFileName 插件内配置文件文件名称
     */
    public static void updateConfig(File configFile, String jarConfigFileName) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        String configVersion = config.getString("configVersion");
        if (configVersion != null && configVersion.equals(PluginUtil.getVersion())) {
            return;
        }

        URL url = Main.class.getClassLoader().getResource(jarConfigFileName);
        if (url == null) {
            return;
        }

        try (InputStream in = url.openStream()) {
            YamlConfiguration jarConfig = YamlConfiguration.loadConfiguration(new StringReader(new String(in.readAllBytes())));
            Set<String> jarConfigKeys = jarConfig.getKeys(true);
            Set<String> configKeys = config.getKeys(true);

            jarConfigKeys.removeAll(configKeys);
            for (String key : jarConfigKeys) {
                config.set(key, jarConfig.get(key));
            }

            config.set("configVersion", PluginUtil.getVersion());
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
