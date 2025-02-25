package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.data.HomeData;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.HomeDataUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        // 输出帮助信息
        if (args.length != 1) {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.delhome.usage"))
                    .replace("{command}", label)
            );
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

