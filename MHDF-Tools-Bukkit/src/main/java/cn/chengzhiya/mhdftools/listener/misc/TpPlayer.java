package cn.chengzhiya.mhdftools.listener.misc;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.teleport.TeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public final class TpPlayer extends AbstractListener {
    private final HashMap<String, Integer> autoTryHashMap = new HashMap<>();

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

        TeleportUtil.teleport(player, targetPlayer.getLocation(), autoTryHashMap);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        String targetPlayerName = Main.instance.getCacheManager().get(player.getName() + "_tpPlayer");
        if (targetPlayerName == null) {
            return;
        }

        Main.instance.getCacheManager().remove(player.getName() + "_tpPlayer");
    }
}
