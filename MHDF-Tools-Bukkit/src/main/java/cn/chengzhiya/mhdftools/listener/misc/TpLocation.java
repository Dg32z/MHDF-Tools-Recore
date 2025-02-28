package cn.chengzhiya.mhdftools.listener.misc;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.BungeeCordLocation;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.runnable.MHDFRunnable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class TpLocation extends AbstractListener {
    public TpLocation() {
        super(null);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String tpLocationBase64 = Main.instance.getCacheManager().get(player.getName() + "_tpLocation");
        if (tpLocationBase64 == null) {
            return;
        }

        MHDFRunnable runnable = new MHDFRunnable() {
            @Override
            public void run() {
                Main.instance.getCacheManager().remove(player.getName() + "_tpLocation");
                BungeeCordLocation tpLocation = new BungeeCordLocation(tpLocationBase64);
                if (!tpLocation.getServer().equals(Main.instance.getBungeeCordManager().getServerName())) {
                    return;
                }

                player.teleport(tpLocation.toLocation());
            }
        };

        if (!ConfigUtil.getConfig().getBoolean("bungeeCordSettings.teleportDelay.enable")) {
            runnable.run();
            return;
        }

        runnable.runTaskLater(Main.instance, ConfigUtil.getConfig().getInt("bungeeCordSettings.teleportDelay.delay"));
    }
}
