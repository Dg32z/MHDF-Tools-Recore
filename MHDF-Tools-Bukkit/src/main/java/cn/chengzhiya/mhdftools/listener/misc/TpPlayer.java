package cn.chengzhiya.mhdftools.listener.misc;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.scheduler.MHDFScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

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

    /**
     * 传送玩家
     *
     * @param player       被传送的玩家实例
     * @param targetPlayer 传送到的玩家实例
     */
    private void teleport(Player player, Player targetPlayer) {
        boolean result = player.teleport(targetPlayer);
        if (result) {
            autoTryHashMap.remove(player.getName());
            return;
        }

        int times = autoTryHashMap.get(player.getName()) != null ? autoTryHashMap.get(player.getName()) : 0;
        if (times >= ConfigUtil.getConfig().getInt("bungeeCordSettings.autoTry.maxTimes")) {
            autoTryHashMap.remove(player.getName());
            return;
        }

        autoTryHashMap.put(player.getName(), times + 1);

        MHDFScheduler.getGlobalRegionScheduler().runDelayed(Main.instance, (task) ->
                        teleport(player, targetPlayer),
                ConfigUtil.getConfig().getInt("bungeeCordSettings.autoTry.delay")
        );
    }
}
