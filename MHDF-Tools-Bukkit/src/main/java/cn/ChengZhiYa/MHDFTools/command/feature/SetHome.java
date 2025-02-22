package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.entity.data.HomeData;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.database.HomeDataUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.HomeUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class SetHome extends AbstractCommand {
    public SetHome() {
        super(
                "homeSettings.enable",
                "设置家",
                "mhdftools.commands.sethome",
                true,
                ConfigUtil.getConfig().getStringList("homeSettings.sethomeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.sethome.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        int maxHome = HomeUtil.getMaxHome(sender);
        if (HomeDataUtil.getHomeDataList(sender).size() >= maxHome) {
            sender.sendMessage(LangUtil.i18n("commands.sethome.isMax")
                    .replace("{amount}", String.valueOf(maxHome))
            );
            return;
        }

        Location location = sender.getLocation();

        HomeData homeData = HomeDataUtil.getHomeData(sender, args[0]);
        homeData.setServer(BungeeCordUtil.getServerName());
        homeData.setWorld(location.getWorld().getName());
        homeData.setX(location.getX());
        homeData.setY(location.getY());
        homeData.setZ(location.getZ());
        homeData.setYaw(location.getYaw());
        homeData.setPitch(location.getPitch());

        HomeDataUtil.updateHomeData(homeData);
        sender.sendMessage(LangUtil.i18n("commands.sethome.message")
                .replace("{home}", args[0])
        );
    }
}
