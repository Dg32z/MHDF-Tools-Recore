package cn.chengzhiya.mhdftools.util.teleport;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public final class TeleportUtil {

    /**
     * 传送玩家
     *
     * @param player   玩家实例
     * @param location 位置实例
     * @param map      用于记录玩家传送尝试次数的映射表。
     */
    public static void teleport(Player player, Location location, ConcurrentHashMap<String, Integer> map) {
        player.teleportAsync(location).thenAccept(success -> {
            location.setPitch(Math.max(-90f, Math.min(90f, location.getPitch())));

            int times = map.getOrDefault(player.getName(), 0);
            int maxTimes = ConfigUtil.getConfig().getInt("bungeecord.autoTry.maxTimes");

            if (success) {
                if (player.getLocation().getWorld() != location.getWorld() || player.getLocation().distance(location) < 5.0) {
                    map.remove(player.getName());
                    return;
                }
            }

            if (times >= maxTimes) {
                map.remove(player.getName());
                return;
            }

            map.put(player.getName(), times + 1);
            int delay = ConfigUtil.getConfig().getInt("bungeecord.autoTry.delay");
            MHDFScheduler.getGlobalRegionScheduler().runTaskLater(Main.instance, () ->
                    teleport(player, location, map), delay);
        });
    }
}
