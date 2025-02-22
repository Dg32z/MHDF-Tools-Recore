package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.entity.BungeeCordLocation;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        String backLocationBase64 = Main.instance.getCacheManager().get(sender.getName() + "_back");

        if (backLocationBase64 == null) {
            sender.sendMessage(LangUtil.i18n("commands.back.noLocation"));
            return;
        }

        BungeeCordUtil.teleportLocation(sender.getName(), new BungeeCordLocation(backLocationBase64));
        sender.sendMessage(LangUtil.i18n("commands.back.message"));
    }
}
