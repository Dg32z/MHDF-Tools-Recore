package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.task.AbstractTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public final class ChatDelay extends AbstractTask {
    public ChatDelay() {
        super(
                "chatSettings.enable",
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String delayData = Main.instance.getCacheManager().get("chatDelay", player.getName());
            if (delayData == null) {
                continue;
            }

            int delay = Integer.parseInt(delayData);
            if (delay <= 0) {
                Main.instance.getCacheManager().remove("chatDelay", player.getName());
                continue;
            }

            delay--;
            Main.instance.getCacheManager().put("chatDelay", player.getName(), String.valueOf(delay));
        }
    }
}
