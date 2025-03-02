package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.BungeeCordLocation;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class Back extends AbstractCommand {
    public Back() {
        super(
                "backSettings.enable",
                "返回死亡位置",
                "mhdftools.commands.back",
                true,
                ConfigUtil.getConfig().getStringList("backSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.back.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigUtil.getConfig().getStringList("backSettings.blackWorld").contains(sender.getWorld().getName())) {
            sender.sendMessage(LangUtil.i18n("blackWorld"));
            return;
        }

        String backLocationBase64 = Main.instance.getCacheManager().get(sender.getName() + "_back");
        if (backLocationBase64 == null) {
            sender.sendMessage(LangUtil.i18n("commands.back.noLocation"));
            return;
        }

        Main.instance.getBungeeCordManager().teleportLocation(sender.getName(), new BungeeCordLocation(backLocationBase64));
        sender.sendMessage(LangUtil.i18n("commands.back.message"));
    }
}
