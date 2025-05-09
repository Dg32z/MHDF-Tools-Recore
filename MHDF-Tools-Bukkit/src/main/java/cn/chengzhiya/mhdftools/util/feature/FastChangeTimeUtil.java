package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class FastChangeTimeUtil {
    /**
     * 获取命令列表
     *
     * @return 命令列表
     */
    public static List<String> getCommandList() {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("fastChangeTimeSettings.time");
        if (config == null) {
            return new ArrayList<>();
        }

        List<String> commandList = new ArrayList<>();
        for (String key : config.getKeys(false)) {
            ConfigurationSection time = config.getConfigurationSection(key);
            if (time == null) {
                continue;
            }

            commandList.addAll(time.getStringList("commands"));
        }

        return commandList;
    }
}
