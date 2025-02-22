package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.entity.data.HomeData;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.database.HomeDataUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class DelHome extends AbstractCommand {
    public DelHome() {
        super(
                "homeSettings.enable",
                "删除家",
                "mhdftools.commands.delhome",
                true,
                ConfigUtil.getConfig().getStringList("homeSettings.delhomeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return;
        }

        if (!HomeDataUtil.ifHomeDataExist(sender, args[0])) {
            sender.sendMessage(LangUtil.i18n("commands.delhome.noHome"));
            return;
        }

        HomeData homeData = HomeDataUtil.getHomeData(sender, args[0]);
        HomeDataUtil.removeHomeData(homeData);
        sender.sendMessage(LangUtil.i18n("commands.delhome.message")
                .replace("{home}", args[0])
        );
        sender.sendMessage(LangUtil.i18n("usageError")
                .replace("{usage}", LangUtil.i18n("commands.delhome.usage"))
                .replace("{command}", label)
        );
    }
}

