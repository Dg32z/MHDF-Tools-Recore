package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.NickUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Nick extends AbstractCommand {
    public Nick() {
        super(
                "nickSettings.enable",
                "匿名",
                "mhdftools.commands.nick",
                false,
                ConfigUtil.getConfig().getStringList("nickSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;

        // 修改玩家自己的匿名名称
        if (args.length == 1 && sender instanceof Player) {
            player = (Player) sender;
        }

        // 修改其他玩家的匿名名称
        if (args.length >= 2) {
            if (Bukkit.getPlayer(args[1]) == null) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
                return;
            }
            if (!sender.hasPermission("mhdftools.commands.nick.give")) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("noPermission"));
                return;
            }
            player = Bukkit.getPlayer(args[1]);
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.nick.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (args[0].equals("off")) {
            NickUtil.resetNickName(player);
        } else {
            NickUtil.setNickName(player, ColorUtil.miniMessage(args[0]));
        }

        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.nick.message")
                .replace("{player}", NickUtil.getName(player))
                .replace("{name}", ColorUtil.color(args[0]))
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("off");
        }
        if (args.length == 2) {
            return Main.instance.getBungeeCordManager().getBukkitPlayerList();
        }
        return new ArrayList<>();
    }
}
