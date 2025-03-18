package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.data.HomeData;
import cn.chengzhiya.mhdftools.menu.feature.HomeMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.HomeDataUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Home extends AbstractCommand {
    public Home() {
        super(
                "homeSettings.enable",
                "传送到指定家",
                "mhdftools.commands.home",
                true,
                ConfigUtil.getConfig().getStringList("homeSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigUtil.getConfig().getStringList("homeSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("blackWorld"));
            return;
        }

        if (args.length == 0) {
            new HomeMenu(sender, 1).openMenu();
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.home.openMenuMessage"));
            return;
        }

        if (args.length == 1) {
            if (!HomeDataUtil.ifHomeDataExist(sender, args[0])) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.home.noHome"));
                return;
            }

            HomeData homeData = HomeDataUtil.getHomeData(sender, args[0]);
            Main.instance.getBungeeCordManager().teleportLocation(sender, homeData.toBungeeCordLocation());

            Main.instance.getBungeeCordManager().sendMessage(sender, LangUtil.i18n("commands.home.teleportMessage")
                    .replace("{home}", homeData.getHome())
            );
            return;
        }

        // 输出帮助信息
        {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.home.usage"))
                    .replace("{command}", label)
            );
        }
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
