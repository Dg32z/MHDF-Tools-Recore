package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.task.AbstractTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
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
                Main.instance.getCacheManager().remove(player.getName() + "_tpaPlayer");
                return;
            }

            delay--;
            Main.instance.getCacheManager().put(player.getName() + "_tpaDelay", String.valueOf(delay));
        }
    }
}
