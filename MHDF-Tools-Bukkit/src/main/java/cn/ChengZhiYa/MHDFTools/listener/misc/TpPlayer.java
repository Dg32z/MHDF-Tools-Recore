package cn.ChengZhiYa.MHDFTools.listener.misc;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
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

        Main.instance.getCacheManager().remove(player.getName() + "_tpPlayer");
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            return;
        }

        player.teleport(targetPlayer);
    }
}
