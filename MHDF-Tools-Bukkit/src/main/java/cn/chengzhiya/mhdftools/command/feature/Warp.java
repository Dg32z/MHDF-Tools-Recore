package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.data.WarpData;
import cn.chengzhiya.mhdftools.util.BungeeCordUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.WarpDataUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class Warp extends AbstractCommand {
    public Warp() {
        super(
                "warpSettings.enable",
                "传送到指定传送点",
                "mhdftools.commands.warp",
                true,
                ConfigUtil.getConfig().getStringList("warpSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.warp.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (!WarpDataUtil.ifWarpDataExist(args[0])) {
            sender.sendMessage(LangUtil.i18n("commands.warp.noWarp"));
            return;
        }

        WarpData warpData = WarpDataUtil.getWarpData(args[0]);
        BungeeCordUtil.teleportLocation(sender.getName(), warpData.toBungeeCordLocation());

        sender.sendMessage(LangUtil.i18n("commands.warp.message")
                .replace("{warp}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return WarpDataUtil.getHomeDataList().stream()
                    .map(WarpData::getWarp)
                    .toList();
        }
        return new ArrayList<>();
    }
}
