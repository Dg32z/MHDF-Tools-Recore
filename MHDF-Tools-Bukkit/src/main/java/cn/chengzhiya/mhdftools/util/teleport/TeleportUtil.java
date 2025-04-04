package cn.chengzhiya.mhdftools.util.teleport;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.scheduler.MHDFScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public final class TeleportUtil {

    /**
     * 传送玩家
     *
     * @param player   需要传送的玩家实例，不可为null
     * @param location 目标位置坐标，不可为null
     * @param map      用于记录玩家传送尝试次数的映射表，当传送成功或超过最大重试次数时移除对应条目。
     *                 键为玩家名称，值为当前已尝试次数。
     */
    public static void teleport(Player player, Location location, HashMap<String, Integer> map) {
        player.teleportAsync(location).thenAccept(success -> {
            if (success) {
                map.remove(player.getName());
            } else {
                int times = map.getOrDefault(player.getName(), 0);
                int maxTimes = ConfigUtil.getConfig().getInt("bungeecord.autoTry.maxTimes");
                if (times >= maxTimes) {
                    map.remove(player.getName());
                } else {
                    map.put(player.getName(), times + 1);
                    int delay = ConfigUtil.getConfig().getInt("bungeecord.autoTry.delay");
                    MHDFScheduler.getGlobalRegionScheduler().runDelayed(Main.instance, (task) ->
                            teleport(player, location, map), delay);
                }
            }
        });
    }
}
