package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.entity.data.HomeData;
import cn.ChengZhiYa.MHDFTools.menu.fastuse.HomeMenu;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.database.HomeDataUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
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
        if (args.length == 0) {
            new HomeMenu(sender, 1).openMenu();
            sender.sendMessage(LangUtil.i18n("commands.home.openMenuMessage"));
            return;
        }
        if (args.length == 1) {
            if (!HomeDataUtil.ifHomeDataExist(sender, args[0])) {
                sender.sendMessage(LangUtil.i18n("commands.home.noHome"));
                return;
            }

            HomeData homeData = HomeDataUtil.getHomeData(sender, args[0]);
            BungeeCordUtil.teleportLocation(sender.getName(), homeData.toBungeeCordLocation());

            sender.sendMessage(LangUtil.i18n("commands.home.teleportMessage")
                    .replace("{home}", homeData.getHome())
            );
            return;
        }
        sender.sendMessage(LangUtil.i18n("usageError")
                .replace("{usage}", LangUtil.i18n("commands.home.usage"))
                .replace("{command}", label)
        );
    }
}
