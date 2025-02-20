package cn.ChengZhiYa.MHDFTools.listener.feature;

import cn.ChengZhiYa.MHDFTools.entity.data.NickData;
import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.util.database.NickDataUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.NickUtil;
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
