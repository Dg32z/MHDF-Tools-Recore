package cn.ChengZhiYa.MHDFTools.listener.misc;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.entity.BungeeCordLocation;
import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class TpLocation extends AbstractListener {
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
        if (tpLocation.getServer().equals(BungeeCordUtil.getServerName())) {
            return;
        }

        player.teleport(tpLocation.toLocation());
    }
}
