package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.entity.data.WarpData;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.database.WarpDataUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class SetWarp extends AbstractCommand {
    public SetWarp() {
        super(
                "warpSettings.enable",
                "设置传送点",
                "mhdftools.commands.setwarp",
                true,
                ConfigUtil.getConfig().getStringList("warpSettings.setwarpCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.setwarp.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Location location = sender.getLocation();

        WarpData warpData = WarpDataUtil.getWarpData(args[0]);
        warpData.setServer(BungeeCordUtil.getServerName());
        warpData.setWorld(location.getWorld().getName());
        warpData.setX(location.getX());
        warpData.setY(location.getY());
        warpData.setZ(location.getZ());
        warpData.setYaw(location.getYaw());
        warpData.setPitch(location.getPitch());

        WarpDataUtil.updateWarpData(warpData);
        sender.sendMessage(LangUtil.i18n("commands.setwarp.message")
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
