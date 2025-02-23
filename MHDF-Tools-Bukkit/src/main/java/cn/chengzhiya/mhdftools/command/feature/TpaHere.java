package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.enums.MessageType;
import cn.chengzhiya.mhdftools.util.BungeeCordUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
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
        if (args.length == 1) {
            if (!BungeeCordUtil.ifPlayerOnline(args[0])) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }

            Main.instance.getCacheManager().put(sender.getName() + "_tpaherePlayer", args[0]);
            Main.instance.getCacheManager().put(sender.getName() + "_tpahereDelay", String.valueOf(ConfigUtil.getConfig().getInt("tpahereSettings.delay")));

            BungeeCordUtil.sendMessage(args[0], MessageType.MINI_MESSAGE, LangUtil.i18n("commands.tpahere.requestMessage")
                    .replace("{player}", sender.getName())
            );

            sender.sendMessage(LangUtil.i18n("commands.tpahere.message")
                    .replace("{player}", sender.getName())
            );
            return;
        }
        if (args.length == 2) {
            String targetPlayerName = Main.instance.getCacheManager().get(args[1] + "_tpPlayer");
            if (targetPlayerName == null) {
                sender.sendMessage(LangUtil.i18n("commands.tpahere.reject.noRequest"));
                return;
            }

            if (!targetPlayerName.equals(sender.getName())) {
                sender.sendMessage(LangUtil.i18n("commands.tpahere.reject.noRequest"));
                return;
            }

            Main.instance.getCacheManager().remove(args[1] + "_tpaherePlayer");
            Main.instance.getCacheManager().remove(args[1] + "_tpahereDelay");

            if (!BungeeCordUtil.ifPlayerOnline(args[1])) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }

            switch (args[0]) {
                case "accept" -> {
                    BungeeCordUtil.teleportPlayerServer(sender.getName(), args[1]);
                    Main.instance.getCacheManager().put(sender.getName() + "_tpPlayer", args[1]);
                    BungeeCordUtil.sendMessage(args[0], MessageType.LEGACY, LangUtil.i18n("commands.tpahere.accept.accepted")
                            .replace("{player}", args[1])
                    );

                    sender.sendMessage(LangUtil.i18n("commands.tpahere.accept.message")
                            .replace("{player}", args[1])
                    );
                    return;
                }
                case "reject" -> {
                    BungeeCordUtil.sendMessage(args[0], MessageType.LEGACY, LangUtil.i18n("commands.tpahere.reject.rejected")
                            .replace("{player}", args[1])
                    );

                    sender.sendMessage(LangUtil.i18n("commands.tpahere.reject.message")
                            .replace("{player}", args[1])
                    );
                    return;
                }
            }
        }

        {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.tpahere.usage"))
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