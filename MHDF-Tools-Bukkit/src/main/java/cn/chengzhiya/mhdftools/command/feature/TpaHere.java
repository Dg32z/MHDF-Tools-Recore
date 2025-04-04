package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.enums.TeleportRequestType;
import cn.chengzhiya.mhdftools.menu.feature.TeleportRequestMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.TpaHereUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class TpaHere extends AbstractCommand {
    public TpaHere() {
        super(
                "tpahereSettings.enable",
                "请求指定玩家传送到当前位置",
                "mhdftools.commands.tpahere",
                true,
                ConfigUtil.getConfig().getStringList("tpahereSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigUtil.getConfig().getStringList("tpahereSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("blackWorld"));
            return;
        }

        if (args.length == 0) {
            new TeleportRequestMenu(sender, TeleportRequestType.TPAHERE, 1).openMenu();
            return;
        }
        if (args.length == 1) {
            TpaHereUtil.sendTpaHereRequest(sender, args[0]);
            return;
        }
        if (args.length == 2) {
            String targetPlayerName = Main.instance.getCacheManager().get(args[1] + "_tpaherePlayer");
            if (targetPlayerName == null) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.tpahere.noRequest"));
                return;
            }

            if (!targetPlayerName.equals(sender.getName())) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.tpahere.noRequest"));
                return;
            }

            Main.instance.getCacheManager().remove(args[1] + "_tpaherePlayer");
            Main.instance.getCacheManager().remove(args[1] + "_tpahereDelay");

            if (!Main.instance.getBungeeCordManager().ifPlayerOnline(args[1])) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
                return;
            }

            switch (args[0]) {
                case "accept" -> {
                    Main.instance.getBungeeCordManager().teleportPlayer(sender.getName(), args[1]);
                    Main.instance.getBungeeCordManager().sendMessage(args[0], LangUtil.i18n("commands.tpahere.accept.accepted")
                            .replace("{player}", args[1])
                    );

                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.tpahere.accept.message")
                            .replace("{player}", args[1])
                    );
                    return;
                }
                case "reject" -> {
                    Main.instance.getBungeeCordManager().sendMessage(args[0], LangUtil.i18n("commands.tpahere.reject.rejected")
                            .replace("{player}", args[1])
                    );

                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.tpahere.reject.message")
                            .replace("{player}", args[1])
                    );
                    return;
                }
            }
        }

        {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.tpahere.usage"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}