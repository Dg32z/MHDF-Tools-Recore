package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.BungeeCordLocation;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Spawn extends AbstractCommand {
    public Spawn() {
        super(
                "spawnSettings.enable",
                "返回出生点",
                "mhdftools.commands.spawn",
                true,
                ConfigUtil.getConfig().getStringList("spawnSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.spawn.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigUtil.getConfig().getStringList("spawnSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("blackWorld"));
            return;
        }

        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("spawnSettings.location");
        if (config == null) {
            return;
        }

        BungeeCordLocation bungeeCordLocation = new BungeeCordLocation(
                config.getString("server"),
                config.getString("world"),
                config.getDouble("x"),
                config.getDouble("y"),
                config.getDouble("z"),
                (float) config.getDouble("yaw"),
                (float) config.getDouble("pitch")
        );

        Main.instance.getBungeeCordManager().teleportLocation(sender, bungeeCordLocation);
        Main.instance.getBungeeCordManager().sendMessage(sender, LangUtil.i18n("commands.spawn.message"));
    }
}
