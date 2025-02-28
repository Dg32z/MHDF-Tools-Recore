package cn.chengzhiya.mhdftools.listener.misc;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.runnable.MHDFRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class TpPlayer extends AbstractListener {
    public TpPlayer() {
        super(null);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String targetPlayerName = Main.instance.getCacheManager().get(player.getName() + "_tpPlayer");
        if (targetPlayerName == null) {
            return;
        }

        MHDFRunnable runnable = new MHDFRunnable() {
            @Override
            public void run() {
                Main.instance.getCacheManager().remove(player.getName() + "_tpPlayer");
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                if (targetPlayer == null) {
                    return;
                }

                player.teleport(targetPlayer);
            }
        };

        if (!ConfigUtil.getConfig().getBoolean("bungeeCordSettings.teleportDelay.enable")) {
            runnable.run();
            return;
        }

        runnable.runTaskLater(Main.instance, ConfigUtil.getConfig().getInt("bungeeCordSettings.teleportDelay.delay"));
    }
}
