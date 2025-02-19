package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.CrashUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public final class Crash extends AbstractCommand {
    public Crash() {
        super(
                "crashSettings.enable",
                "崩溃玩家客户端",
                "mhdftools.commands.crash",
                false,
                ConfigUtil.getConfig().getStringList("crashSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }

            String crashType = args.length == 1
                    ? ConfigUtil.getConfig().getString("crashSettings.defaultType")
                    : args[1];

            if (crashType != null && CrashUtil.crashPlayerClient(player, crashType)) {
                sender.sendMessage(LangUtil.i18n("commands.crash.done"));
            } else {
                sender.sendMessage(LangUtil.i18n("commands.crash.typeNotExists"));
            }
            return;
        }

        sender.sendMessage(LangUtil.i18n("usageError")
                .replace("{usage}", LangUtil.i18n("commands.crash.usage"))
                .replace("{command}", label)
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return BungeeCordUtil.getPlayerList();
        }
        if (args.length == 2) {
            return Arrays.asList("explosion", "changeHoldItem", "posAndLook", "invalidParticle");
        }
        return new ArrayList<>();
    }
}
