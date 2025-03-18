package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.data.WarpData;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.WarpDataUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class DelWarp extends AbstractCommand {
    public DelWarp() {
        super(
                "warpSettings.enable",
                "删除传送点",
                "mhdftools.commands.delwarp",
                true,
                ConfigUtil.getConfig().getStringList("warpSettings.delwarpCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.delwarp.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (!WarpDataUtil.ifWarpDataExist(args[0])) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.delwarp.noWarp"));
            return;
        }

        WarpData warpData = WarpDataUtil.getWarpData(args[0]);
        WarpDataUtil.removeWarpData(warpData);

        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.delwarp.message")
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
