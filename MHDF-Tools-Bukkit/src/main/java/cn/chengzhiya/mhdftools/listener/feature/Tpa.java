package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public final class Tpa extends AbstractListener {
    public Tpa() {
        super(
                "tpaSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Main.instance.getCacheManager().remove("tpaPlayer", player.getName());
        Main.instance.getCacheManager().remove("tpaDelay", player.getName());
    }
}
