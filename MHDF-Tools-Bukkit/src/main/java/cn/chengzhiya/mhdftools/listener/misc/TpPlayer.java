package cn.chengzhiya.mhdftools.listener.misc;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.scheduler.MHDFScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

        teleport(player, targetPlayer);
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

    /**
     * 传送玩家到目标玩家的位置
     *
     * @param player       被传送的玩家实例
     * @param targetPlayer 目标玩家实例
     */
    private void teleport(Player player, Player targetPlayer) {
        Location targetLocation = targetPlayer.getLocation();
        player.teleportAsync(targetLocation).thenAccept(success -> {
            if (success) {
                autoTryHashMap.remove(player.getName());
            } else {
                int times = autoTryHashMap.getOrDefault(player.getName(), 0);
                int maxTimes = ConfigUtil.getConfig().getInt("bungeecord.autoTry.maxTimes");
                if (times >= maxTimes) {
                    autoTryHashMap.remove(player.getName());
                } else {
                    autoTryHashMap.put(player.getName(), times + 1);
                    int delay = ConfigUtil.getConfig().getInt("bungeecord.autoTry.delay");
                    MHDFScheduler.getGlobalRegionScheduler().runDelayed(Main.instance, (task) ->
                            teleport(player, targetPlayer), delay);
                }
            }
        });
    }
}
