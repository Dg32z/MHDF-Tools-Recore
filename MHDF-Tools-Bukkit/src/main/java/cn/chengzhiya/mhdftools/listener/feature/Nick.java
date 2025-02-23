package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.entity.data.NickData;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.database.NickDataUtil;
import cn.chengzhiya.mhdftools.util.feature.NickUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class Nick extends AbstractListener {
    public Nick() {
        super(
                "nickSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        NickData nickData = NickDataUtil.getNickData(player);
        if (nickData.getNick() == null) {
            return;
        }

        NickUtil.setNickDisplay(player, nickData.getNick());
    }
}
