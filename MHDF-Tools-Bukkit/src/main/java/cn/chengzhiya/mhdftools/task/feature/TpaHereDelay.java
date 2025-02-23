package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.task.AbstractTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class TpaHereDelay extends AbstractTask {
    public TpaHereDelay() {
        super(
                "tpahereSettings.enable",
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String delayString = Main.instance.getCacheManager().get(player.getName() + "_tpahereDelay");
            if (delayString == null) {
                continue;
            }

            int delay = Integer.parseInt(delayString);
            if (delay <= 0) {
                Main.instance.getCacheManager().remove(player.getName() + "_tpahereDelay");
                return;
            }

            delay--;
            Main.instance.getCacheManager().put(player.getName() + "_tpahereDelay", String.valueOf(delay));
        }
    }
}
