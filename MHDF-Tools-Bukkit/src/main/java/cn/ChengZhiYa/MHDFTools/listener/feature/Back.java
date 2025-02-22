package cn.ChengZhiYa.MHDFTools.listener.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.entity.BungeeCordLocation;
import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class Back extends AbstractListener {
    public Back() {
        super(
                "backSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();

        BungeeCordLocation bungeeCordLocation = new BungeeCordLocation(
                BungeeCordUtil.getServerName(),
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );

        Main.instance.getCacheManager().put(player.getName() + "_back", bungeeCordLocation.toBase64());
    }
}
