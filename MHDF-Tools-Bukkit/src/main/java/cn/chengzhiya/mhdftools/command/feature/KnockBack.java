package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.BungeeCordUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class KnockBack extends AbstractCommand {
    public KnockBack() {
        super(
                "knockBackSettings.enable",
                "崩溃玩家客户端",
                "mhdftools.commands.knockback",
                false,
                ConfigUtil.getConfig().getStringList("knockBackSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.knockback.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(LangUtil.i18n("playerOffline"));
            return;
        }

        double x = ConfigUtil.getConfig().getDouble("knockBackSettings.vector.x");
        double y = ConfigUtil.getConfig().getDouble("knockBackSettings.vector.y");
        double z = ConfigUtil.getConfig().getDouble("knockBackSettings.vector.z");

        Vector vector = new Vector(x, y, z);
        player.setVelocity(vector);
        sender.sendMessage(LangUtil.i18n("commands.knockback.message")
                .replace("{player}", player.getName())
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return BungeeCordUtil.getPlayerList();
        }
        return new ArrayList<>();
    }
}