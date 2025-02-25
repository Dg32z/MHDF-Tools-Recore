package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.data.FlyStatus;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.FlyStatusUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public final class FlyTime extends AbstractCommand {
    public FlyTime() {
        super(
                "flySettings.enable",
                "限时飞行",
                "mhdftools.commands.flytime",
                false,
                ConfigUtil.getConfig().getStringList("flySettings.flytimeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            Player player = Bukkit.getPlayer(args[1]);
            long inputTime = Long.parseLong(args[2]);
            if (player == null) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }

            FlyStatus flyStatus = FlyStatusUtil.getFlyStatus(player);

            // 设置飞行时间
            if (args[0].equalsIgnoreCase("set")) {
                flyStatus.setTime(inputTime);
                FlyStatusUtil.updateFlyStatus(flyStatus);

                sender.sendMessage(LangUtil.i18n("commands.flytime.subCommands.set.message")
                        .replace("{player}", player.getName())
                        .replace("{time}", args[2])
                );
                return;
            }

            // 增加飞行时间
            if (args[0].equalsIgnoreCase("add")) {
                long time = flyStatus.getTime() + inputTime;
                flyStatus.setTime(time);
                FlyStatusUtil.updateFlyStatus(flyStatus);

                sender.sendMessage(LangUtil.i18n("commands.flytime.subCommands.add.message")
                        .replace("{player}", player.getName())
                        .replace("{add}", args[2])
                        .replace("{time}", String.valueOf(time))
                );
                return;
            }

            // 减少飞行时间
            if (args[0].equalsIgnoreCase("take")) {
                long time = flyStatus.getTime() - inputTime;
                flyStatus.setTime(time);
                FlyStatusUtil.updateFlyStatus(flyStatus);

                sender.sendMessage(LangUtil.i18n("commands.flytime.subCommands.take.message")
                        .replace("{player}", player.getName())
                        .replace("{take}", args[2])
                        .replace("{time}", String.valueOf(time))
                );
                return;
            }
        }

        {
            sender.sendMessage(LangUtil.i18n("commands.flytime.subCommands.help.message")
                    .replace("{helpList}", LangUtil.getHelpList("flytime"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2 &&
                (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take"))
        ) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        if (args.length == 1) {
            return Arrays.asList("help", "set", "add", "take");
        }
        return new ArrayList<>();
    }
}
