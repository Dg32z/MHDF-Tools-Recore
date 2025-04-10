package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.entity.database.VanishStatus;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.database.VanishStatusUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class VanishJoinQuit extends AbstractListener {
    public VanishJoinQuit() {
        super(
                "vanishSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
     Player player = event.getPlayer();
        VanishStatus vanishStatus = VanishStatusUtil.getVanishStatus(player);
        if (!vanishStatus.isEnable()) {
            return;
        }

        event.setJoinMessage(null);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        VanishStatus vanishStatus = VanishStatusUtil.getVanishStatus(player);
        if (!vanishStatus.isEnable()) {
            return;
        }

        event.setQuitMessage(null);
    }
}
