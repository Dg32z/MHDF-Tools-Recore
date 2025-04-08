package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.InvseeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Invsee extends AbstractCommand {
    public Invsee() {
        super(
                "invseeSettings.enable",
                "查看背包",
                "mhdftools.commands.invsee",
                true,
                ConfigUtil.getConfig().getStringList("invseeSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 2) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.invsee.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
            return;
        }

        String inventoryType = args[1];
        if (InvseeUtil.invsee(sender, target, inventoryType)) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.invsee.message")
                    .replace("{type}", LangUtil.i18n("commands.invsee.types." + inventoryType))
            );
        } else {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.invsee.typeNotExists"));
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        if (args.length == 2) {
            return Arrays.asList("inventory", "enderchest");
        }
        return new ArrayList<>();
    }
}
