package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class Suicide extends AbstractCommand {
    public Suicide() {
        super(
                "suicideSettings.enable",
                "自杀",
                "mhdftools.commands.suicide",
                true,
                ConfigUtil.getConfig().getStringList("suicideSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.suicide.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigUtil.getConfig().getStringList("suicideSettings.blackWorld").contains(sender.getWorld().getName())) {
            sender.sendMessage(LangUtil.i18n("blackWorld"));
            return;
        }

        sender.setHealth(0.0);
        sender.sendMessage(LangUtil.i18n("commands.suicide.message"));
    }
}
