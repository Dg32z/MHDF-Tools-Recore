package cn.ChengZhiYa.MHDFTools.listener.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
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

        Main.instance.getCacheManager().remove(player.getName() + "_tpaPlayer");
        Main.instance.getCacheManager().remove(player.getName() + "_tpaDelay");
    }
}
