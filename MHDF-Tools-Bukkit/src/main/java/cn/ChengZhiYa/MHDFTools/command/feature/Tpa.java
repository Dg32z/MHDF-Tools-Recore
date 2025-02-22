package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.enums.MessageType;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Tpa extends AbstractCommand {
    public Tpa() {
        super(
                "tpaSettings.enable",
                "请求传送到指定玩家位置",
                "mhdftools.commands.tpa",
                true,
                ConfigUtil.getConfig().getStringList("tpaSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (!BungeeCordUtil.ifPlayerOnline(args[0])) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }

            Main.instance.getCacheManager().put(sender.getName() + "_tpaPlayer", args[0]);
            Main.instance.getCacheManager().put(sender.getName() + "_tpaDelay", String.valueOf(ConfigUtil.getConfig().getInt("tpaSettings.delay")));

            BungeeCordUtil.sendMessage(args[0], MessageType.MINI_MESSAGE, LangUtil.i18n("commands.tpa.requestMessage")
                    .replace("{player}", sender.getName())
            );

            sender.sendMessage(LangUtil.i18n("commands.tpa.message")
                    .replace("{player}", sender.getName())
            );
            return;
        }
        if (args.length == 2) {
            String targetPlayerName = Main.instance.getCacheManager().get(args[1] + "_tpPlayer");
            if (targetPlayerName == null) {
                sender.sendMessage(LangUtil.i18n("commands.tpa.reject.noRequest"));
                return;
            }

            if (!targetPlayerName.equals(sender.getName())) {
                sender.sendMessage(LangUtil.i18n("commands.tpa.reject.noRequest"));
                return;
            }

            Main.instance.getCacheManager().remove(args[1] + "_tpaPlayer");
            Main.instance.getCacheManager().remove(args[1] + "_tpaDelay");

            if (!BungeeCordUtil.ifPlayerOnline(args[1])) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }

            switch (args[0]) {
                case "accept" -> {
                    BungeeCordUtil.teleportPlayerServer(args[1], sender.getName());
                    BungeeCordUtil.sendMessage(args[0], MessageType.LEGACY, LangUtil.i18n("commands.tpa.accept.accepted")
                            .replace("{player}", args[1])
                    );

                    sender.sendMessage(LangUtil.i18n("commands.tpa.accept.message")
                            .replace("{player}", args[1])
                    );
                    return;
                }
                case "reject" -> {
                    BungeeCordUtil.sendMessage(args[0], MessageType.LEGACY, LangUtil.i18n("commands.tpa.reject.rejected")
                            .replace("{player}", args[1])
                    );

                    sender.sendMessage(LangUtil.i18n("commands.tpa.reject.message")
                            .replace("{player}", args[1])
                    );
                    return;
                }
            }
        }

        {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.tpa.usage"))
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