package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.ListUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class List extends AbstractCommand {
    public List() {
        super(
                "listSettings.enable",
                "查看在线列表",
                "mhdftools.commands.list",
                false,
                ConfigUtil.getConfig().getStringList("listSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.list.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.list.message")
                .replace("{tps}", String.valueOf(ListUtil.getTps()))
                .replace("{memory}", String.valueOf(ListUtil.getUsedMemory()))
                .replace("{maxMemory}", String.valueOf(ListUtil.getTotalMemory()))
                .replace("{playerCount}", String.valueOf(Main.instance.getBungeeCordManager().getPlayerList().size()))
                .replace("{maxPlayerCount}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{playerList}", Main.instance.getBungeeCordManager().getPlayerList().toString())
        );
    }
}
