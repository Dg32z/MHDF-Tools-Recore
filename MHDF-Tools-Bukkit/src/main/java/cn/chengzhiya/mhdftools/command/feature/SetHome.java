package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.data.HomeData;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.HomeDataUtil;
import cn.chengzhiya.mhdftools.util.feature.HomeUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
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
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.sethome.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigUtil.getConfig().getStringList("homeSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("blackWorld"));
            return;
        }

        int maxHome = HomeUtil.getMaxHome(sender);
        if (HomeDataUtil.getHomeDataList(sender).size() >= maxHome) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.sethome.isMax")
                    .replace("{amount}", String.valueOf(maxHome))
            );
            return;
        }

        Location location = sender.getLocation();

        HomeData homeData = HomeDataUtil.getHomeData(sender, args[0]);
        homeData.setServer(Main.instance.getBungeeCordManager().getServerName());
        homeData.setWorld(location.getWorld().getName());
        homeData.setX(location.getX());
        homeData.setY(location.getY());
        homeData.setZ(location.getZ());
        homeData.setYaw(location.getYaw());
        homeData.setPitch(location.getPitch());

        HomeDataUtil.updateHomeData(homeData);
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.sethome.message")
                .replace("{home}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return HomeDataUtil.getHomeDataList(player).stream()
                    .map(HomeData::getHome)
                    .toList();
        }
        return new ArrayList<>();
    }
}
