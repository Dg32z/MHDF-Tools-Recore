package cn.ChengZhiYa.MHDFTools.listener.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.entity.BungeeCordLocation;
import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class TpBack extends AbstractListener {
    public TpBack() {
        super(
                "tpbackSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location location = event.getFrom();

        BungeeCordLocation bungeeCordLocation = new BungeeCordLocation(
                BungeeCordUtil.getServerName(),
                location
        );

        Main.instance.getCacheManager().put(player.getName() + "_back", bungeeCordLocation.toBase64());
    }
}
