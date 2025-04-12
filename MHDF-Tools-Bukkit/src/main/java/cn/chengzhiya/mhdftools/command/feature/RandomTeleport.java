package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.RandomTeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class RandomTeleport extends AbstractCommand {
    public RandomTeleport() {
        super(
                "randomTeleportSettings.enable",
                "随机传送",
                "mhdftools.commands.randomteleport",
                false,
                ConfigUtil.getConfig().getStringList("randomTeleportSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        String worldName = null;
        String server = null;

        if (args.length == 0 && sender instanceof Player) {
            player = (Player) sender;
            worldName = player.getWorld().getName();
        }

        if (args.length >= 1) {
            worldName = args[0];
        }

        if (ConfigUtil.getConfig().getStringList("randomTeleportSettings.blackWorld").contains(worldName)) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.subCommands.blackWorld"));
            return;
        }

        if (args.length >= 2) {
            if (Bukkit.getPlayer(args[1]) == null) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
                return;
            }
            if (!sender.hasPermission("mhdftools.commands.randomteleport.other")) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("noPermission"));
                return;
            }
            player = Bukkit.getPlayer(args[1]);
        }

        if (args.length >= 3) {
            server = args[2];
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.randomteleport.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        // 不跨服
        if (server == null) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.subCommands.noWorld"));
                return;
            }

            String group = RandomTeleportUtil.getGroup(player);
            ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("randomTeleportSettings." + group);
            if (config == null) {
                return;
            }

            if (config.getBoolean("delay.enable")) {
                String key = player.getName() + "_randomTeleportDelay";

                if (!player.hasPermission("mhdftools.bypass.randomteleport.delay")) {
                    String delayData = Main.instance.getCacheManager().get(key);
                    if (delayData != null) {
                        ActionUtil.sendMessage(player, LangUtil.i18n("commands.randomteleport.delay")
                                .replace("{delay}", delayData)
                        );
                        return;
                    }
                }

                Main.instance.getCacheManager().put(key, String.valueOf(config.getInt("delay.time")));
            }

            RandomTeleportUtil.randomTeleport(player, world);
            ActionUtil.sendMessage(player, LangUtil.i18n("commands.randomteleport.subCommands.message"));
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.subCommands.message"));
            return;
        }

        Main.instance.getCacheManager().put(player.getName() + "_rtpWorld", worldName);
        Main.instance.getBungeeCordManager().connectServer(player.getName(), server);
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.subCommands.message"));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .toList();
        }
        if (args.length == 2) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}