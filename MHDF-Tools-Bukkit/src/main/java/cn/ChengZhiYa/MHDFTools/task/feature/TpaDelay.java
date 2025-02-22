package cn.ChengZhiYa.MHDFTools.task.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.task.AbstractTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class TpaDelay extends AbstractTask {
    public TpaDelay() {
        super(
                "tpaSettings.enable",
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String delayString = Main.instance.getCacheManager().get(player.getName() + "_tpaDelay");
            if (delayString == null) {
                continue;
            }

            int delay = Integer.parseInt(delayString);
            if (delay <= 0) {
                Main.instance.getCacheManager().remove(player.getName() + "_tpaDelay");
                return;
            }

            delay--;
            Main.instance.getCacheManager().put(player.getName() + "_tpaDelay", String.valueOf(delay));
        }
    }
}
