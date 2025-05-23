package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.FastChangeWeatherUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class FastChangeWeather extends AbstractCommand {
    private final ConcurrentHashMap<String, ConfigurationSection> commandConfigHashMap = new ConcurrentHashMap<>();

    public FastChangeWeather() {
        super(
                "fastChangeWeatherSettings.enable",
                "快速调节天气",
                "mhdftools.commands.fastchangeweather",
                false,
                FastChangeWeatherUtil.getCommandList().toArray(new String[0])
        );

        {
            ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("fastChangeWeatherSettings.weather");
            if (config == null) {
                return;
            }

            for (String key : config.getKeys(false)) {
                ConfigurationSection weather = config.getConfigurationSection(key);
                if (weather == null) {
                    continue;
                }

                for (String command : weather.getStringList("commands")) {
                    getCommandConfigHashMap().put(command, weather);
                }
            }
        }
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        boolean storm = getCommandConfigHashMap().get(label) != null &&
                getCommandConfigHashMap().get(label).getBoolean("storm");
        boolean thunder = getCommandConfigHashMap().get(label) != null &&
                getCommandConfigHashMap().get(label).getBoolean("thunder");

        for (World world : Bukkit.getWorlds()) {
            world.setStorm(storm);
            world.setThundering(thunder);
        }

        sender.sendMessage(LangUtil.i18n("commands.fastchangeweather.message")
                .replace("{storm}", storm ? LangUtil.i18n("enable") : LangUtil.i18n("disable"))
                .replace("{thunder}", thunder ? LangUtil.i18n("enable") : LangUtil.i18n("disable"))
        );
    }
}
