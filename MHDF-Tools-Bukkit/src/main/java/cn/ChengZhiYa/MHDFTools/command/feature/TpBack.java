package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.entity.BungeeCordLocation;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class TpBack extends AbstractCommand {
    public TpBack() {
        super(
                "tpbackSettings.enable",
                "返回传送前的位置",
                "mhdftools.commands.tpback",
                true,
                ConfigUtil.getConfig().getStringList("tpbackSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        String backLocationBase64 = Main.instance.getCacheManager().get(sender.getName() + "_tpback");

        if (backLocationBase64 == null) {
            sender.sendMessage(LangUtil.i18n("commands.tpback.noLocation"));
            return;
        }

        BungeeCordUtil.teleportLocation(sender.getName(), new BungeeCordLocation(backLocationBase64));
        sender.sendMessage(LangUtil.i18n("commands.tpback.message"));
    }
}
