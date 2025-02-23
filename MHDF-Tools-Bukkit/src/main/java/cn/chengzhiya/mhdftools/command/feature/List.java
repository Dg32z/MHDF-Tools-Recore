package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.ListUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
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
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.list.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        sender.sendMessage(LangUtil.i18n("commands.list.message")
                .replace("{tps}", String.valueOf(ListUtil.getTps()))
                .replace("{memory}", String.valueOf(ListUtil.getUsedMemory()))
                .replace("{maxMemory}", String.valueOf(ListUtil.getTotalMemory()))
                .replace("{playerCount}", String.valueOf(BungeeCordUtil.getPlayerList().size()))
                .replace("{maxPlayerCount}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{playerList}", BungeeCordUtil.getPlayerList().toString())
        );
    }
}
