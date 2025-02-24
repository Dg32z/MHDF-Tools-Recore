package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.database.EconomyDataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class Economy extends AbstractListener {
    public Economy() {
        super(
                "economySettings.enable"
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (EconomyDataUtil.ifEconomyDataExist(player)) {
            return;
        }

        EconomyDataUtil.initEconomyData(player);
    }
}
