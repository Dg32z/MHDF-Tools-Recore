package cn.chengzhiya.mhdftools.listener.misc;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.BungeeCordLocation;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.teleport.TeleportUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

        TeleportUtil.teleport(player, tpLocation.toLocation(), autoTryHashMap);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        String tpLocationBase64 = Main.instance.getCacheManager().get(player.getName() + "_tpLocation");
        if (tpLocationBase64 == null) {
            return;
        }

        Main.instance.getCacheManager().remove(player.getName() + "_tpLocation");
    }
}