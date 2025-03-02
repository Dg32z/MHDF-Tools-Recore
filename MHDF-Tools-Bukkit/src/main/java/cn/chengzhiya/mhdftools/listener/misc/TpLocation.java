package cn.chengzhiya.mhdftools.listener.misc;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.BungeeCordLocation;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.scheduler.MHDFScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public final class TpLocation extends AbstractListener {
    private final HashMap<String, Integer> autoTryHashMap = new HashMap<>();

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

        Main.instance.getCacheManager().remove(player.getName() + "_tpLocation");

        BungeeCordLocation tpLocation = new BungeeCordLocation(tpLocationBase64);
        if (!tpLocation.getServer().equals(Main.instance.getBungeeCordManager().getServerName())) {
            return;
        }

        teleport(player, tpLocation.toLocation());
    }

    /**
     * 传送玩家
     *
     * @param player   被传送的玩家实例
     * @param location 传送到的位置实例
     */
    private void teleport(Player player, Location location) {
        boolean result = player.teleport(location);
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
                        teleport(player, location),
                ConfigUtil.getConfig().getInt("bungeeCordSettings.autoTry.delay")
        );
    }
}
