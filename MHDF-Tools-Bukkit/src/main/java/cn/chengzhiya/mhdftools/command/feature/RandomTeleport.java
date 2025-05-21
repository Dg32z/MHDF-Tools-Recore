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

        if (args.length >= 3 && !args[2].equals(Main.instance.getBungeeCordManager().getServerName())) {
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

        ConfigurationSection group = RandomTeleportUtil.getGroupConfigurationSection(player);
        if (group == null) {
            return;
        }

        if (group.getBoolean("delay.enable")) {
            if (!player.hasPermission("mhdftools.bypass.randomteleport.delay")) {
                String delayData = Main.instance.getCacheManager().get("randomTeleportDelay", player.getName());
                if (delayData != null) {
                    ActionUtil.sendMessage(player, LangUtil.i18n("commands.randomteleport.delay")
                            .replace("{delay}", delayData)
                    );
                    return;
                }
            }

            Main.instance.getCacheManager().put("randomTeleportDelay", player.getName(), String.valueOf(group.getInt("delay.time")));
        }

        // 不跨服
        if (server == null) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.noWorld"));
                return;
            }

            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.message"));
            RandomTeleportUtil.randomTeleport(player, world);
            return;
        }

        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.message"));
        Main.instance.getCacheManager().put("randomTeleportWorld", player.getName(), worldName);
        Main.instance.getBungeeCordManager().connectServer(player, server);
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