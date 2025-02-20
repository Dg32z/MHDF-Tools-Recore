package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Suicide extends AbstractCommand {
    public Suicide() {
        super(
                "suicideSettings.enable",
                "自杀",
                "mhdftools.commands.suicide",
                true,
                ConfigUtil.getConfig().getStringList("bedSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        sender.setHealth(0.0);
        sender.sendMessage(LangUtil.i18n("commands.suicide.message"));
    }
}
