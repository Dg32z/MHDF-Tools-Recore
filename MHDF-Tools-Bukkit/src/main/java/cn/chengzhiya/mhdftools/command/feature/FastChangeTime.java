package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.FastChangeTimeUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class FastChangeTime extends AbstractCommand {
    private final ConcurrentHashMap<String, ConfigurationSection> commandConfigHashMap = new ConcurrentHashMap<>();

    public FastChangeTime() {
        super(
                "fastChangeTimeSettings.enable",
                "快速调节时间",
                "mhdftools.commands.fastchangetime",
                false,
                FastChangeTimeUtil.getCommandList().toArray(new String[0])
        );

        {
            ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("fastChangeTimeSettings.time");
            if (config == null) {
                return;
            }

            for (String key : config.getKeys(false)) {
                ConfigurationSection time = config.getConfigurationSection(key);
                if (time == null) {
                    continue;
                }

                for (String command : time.getStringList("commands")) {
                    getCommandConfigHashMap().put(command, time);
                }
            }
        }
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        int time = getCommandConfigHashMap().get(label) != null
                ? getCommandConfigHashMap().get(label).getInt("time") : 0;

        for (World world : Bukkit.getWorlds()) {
            world.setTime(time);
        }

        sender.sendMessage(LangUtil.i18n("commands.fastchangetime.message")
                .replace("{time}", String.valueOf(time))
        );
    }
}
