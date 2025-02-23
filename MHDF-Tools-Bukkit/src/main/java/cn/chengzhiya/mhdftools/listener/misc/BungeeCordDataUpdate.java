package cn.chengzhiya.mhdftools.listener.misc;

import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.BungeeCordUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class BungeeCordDataUpdate extends AbstractListener {
    public BungeeCordDataUpdate() {
        super(
                "bungeeCordSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BungeeCordUtil.updateServerName();
        BungeeCordUtil.updateBungeeCordPlayerList();
    }
}
