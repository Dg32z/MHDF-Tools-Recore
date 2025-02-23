package cn.ChengZhiYa.MHDFTools.listener.feature;

import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
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
