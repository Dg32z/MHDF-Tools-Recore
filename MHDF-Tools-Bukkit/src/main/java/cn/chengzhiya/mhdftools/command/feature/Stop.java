package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import cn.chengzhiya.mhdftools.util.runnable.MHDFRunnable;
import cn.chengzhiya.mhdftools.util.scheduler.MHDFScheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Stop extends AbstractCommand {
    public Stop() {
        super(
                "stopSettings.enable",
                "更好的关服",
                "mhdftools.commands.stop",
                false,
                ConfigUtil.getConfig().getStringList("stopSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length >= 3) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.stop.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        int time;
        try {
            time = args.length >= 1 ? Integer.parseInt(args[0]) :
                    ConfigUtil.getConfig().getInt("stopSettings.defaultCountdown");
        } catch (NumberFormatException e) {
            sender.sendMessage(LangUtil.i18n("commands.stop.timeFormatError"));
            return;
        }

        TextComponent message = args.length == 2 ? ColorUtil.color(args[1]) :
                LangUtil.i18n("commands.stop.defaultMessage");

        new MHDFRunnable() {
            private int countdown = time;

            @Override
            public void run() {
                if (countdown <= 0) {
                    this.cancel();
                    MHDFScheduler.getGlobalRegionScheduler().run(Main.instance, (task) -> Bukkit.savePlayers());

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        MHDFScheduler.getGlobalRegionScheduler().run(Main.instance, (task) ->
                                player.kick(LangUtil.i18n("commands.stop.kickMessage")
                                        .replace("{message}", message)
                                )
                        );
                    }

                    Bukkit.shutdown();
                    return;
                }


                ActionUtil.broadcastMessage(LangUtil.i18n("commands.stop.countdownMessage")
                        .replace("{countdown}", String.valueOf(countdown))
                );
                countdown--;
            }
        }.runTaskTimerAsynchronously(Main.instance, 0L, 20L);
    }
}
