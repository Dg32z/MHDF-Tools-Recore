package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.entity.data.VanishStatus;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class Vanish extends AbstractCommand {
    public Vanish() {
        super(
                "vanishSettings.enable",
                "影身",
                "mhdftools.commands.vanish",
                false,
                ConfigUtil.getConfig().getStringList("vanishSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        boolean sendToSender = true;

        // 切换玩家自己的隐身模式
        if (args.length == 0 && sender instanceof Player) {
            sendToSender = false;
            player = (Player) sender;
        }

        // 切换其他玩家的隐身模式
        if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }
            player = Bukkit.getPlayer(args[0]);

            if (!sender.hasPermission("mhdftools.commands.vanish.give")) {
                sender.sendMessage(LangUtil.i18n("noPermission"));
                return;
            }
        }

        // 切换影身
        if (player != null) {
            VanishStatus vanishStatus = VanishUtil.getVanishStatus(player);
            if (!vanishStatus.isEnable()) {
                VanishUtil.enableVanish(player);
                if (sendToSender) {
                    VanishUtil.sendChangeVanishMessage(sender, player, true);
                }
                VanishUtil.sendChangeVanishMessage(player, player, true);
            } else {
                VanishUtil.disableVanish(player);
                if (sendToSender) {
                    VanishUtil.sendChangeVanishMessage(sender, player, false);
                }
                VanishUtil.sendChangeVanishMessage(player, player, false);
            }
            return;
        }

        // 输出帮助信息
        {
            sender.sendMessage(
                    LangUtil.i18n("usageError")
                            .replace("{usage}", LangUtil.i18n("commands.vanish.usage"))
                            .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return BungeeCordUtil.getPlayerList();
        }
        return new ArrayList<>();
    }
}
