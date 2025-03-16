package cn.chengzhiya.mhdftools.util.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
}
