package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class Bed extends AbstractCommand {
    public Bed() {
        super(
                "bedSettings.enable",
                "回到床的位置",
                "mhdftools.commands.bed",
                true,
                ConfigUtil.getConfig().getStringList("bedSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        Location bedLocation = sender.getBedLocation();

        sender.teleport(bedLocation);
        sender.sendMessage(LangUtil.i18n("commands.bed.message"));
    }
}
