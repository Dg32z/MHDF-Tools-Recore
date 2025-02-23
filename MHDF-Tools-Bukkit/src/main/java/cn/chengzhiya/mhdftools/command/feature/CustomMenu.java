package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.CustomMenuConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.CustomMenuUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class CustomMenu extends AbstractCommand {
    public CustomMenu() {
        super(
                "customMenuSettings.enable",
                "自定义菜单",
                "mhdftools.commands.custommenu",
                true,
                ConfigUtil.getConfig().getStringList("customMenuSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.custommenu.usage"))
                    .replace("{command}", label)
            );
        }

        if (!CustomMenuConfigUtil.getCustomMenuList().contains(args[0])) {
            sender.sendMessage(LangUtil.i18n("commands.custommenu.menuNotExists"));
            return;
        }

        CustomMenuUtil.openCustomMenu(sender, args[0]);
        sender.sendMessage(LangUtil.i18n("commands.custommenu.message")
                .replace("{menu}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return CustomMenuConfigUtil.getCustomMenuList();
        }
        return new ArrayList<>();
    }
}
