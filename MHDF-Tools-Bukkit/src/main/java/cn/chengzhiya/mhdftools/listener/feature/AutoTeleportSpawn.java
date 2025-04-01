package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.feature.SpawnUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class AutoTeleportSpawn extends AbstractListener {
    public AutoTeleportSpawn() {
        super(
                "spawnSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("spawnSettings.autoTeleport");
        if (config == null) {
            return;
        }

        Player player = event.getPlayer();

        if (!config.getBoolean("join")) {
            return;
        }

        SpawnUtil.teleportSpawn(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("spawnSettings.autoTeleport");
        if (config == null) {
            return;
        }

        Player player = event.getPlayer();

        if (!config.getBoolean("respawn")) {
            return;
        }

        SpawnUtil.teleportSpawn(player);
    }
}
