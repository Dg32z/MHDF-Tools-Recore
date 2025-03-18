package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.RandomTeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class RandomTeleport extends AbstractListener {
    public RandomTeleport() {
        super(
                "randomTeleportSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String rtpWorld = Main.instance.getCacheManager().get(player.getName() + "_rtpWorld");
        if (rtpWorld == null) {
            return;
        }

        Main.instance.getCacheManager().remove(player.getName() + "_rtpWorld");

        World world = Bukkit.getWorld(rtpWorld);
        if (world == null) {
            ActionUtil.sendMessage(player, LangUtil.i18n("commands.randomteleport.subCommands.noWorld"));
        }

        RandomTeleportUtil.randomTeleport(player, world);
    }
}
