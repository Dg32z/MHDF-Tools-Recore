package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.entity.database.VanishStatus;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.menu.feature.vanish.BarrelMenu;
import cn.chengzhiya.mhdftools.menu.feature.vanish.ChestMenu;
import cn.chengzhiya.mhdftools.menu.feature.vanish.ShulkerBoxMenu;
import cn.chengzhiya.mhdftools.util.database.VanishStatusUtil;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class VanishOpen extends AbstractListener {
    public VanishOpen() {
        super(
                "vanishSettings.enable"
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }
        Block block = event.getClickedBlock();

        VanishStatus vanishStatus = VanishStatusUtil.getVanishStatus(player);
        if (!vanishStatus.isEnable()) {
            return;
        }

        if (block instanceof Chest chest) {
            new ChestMenu(player, chest).openMenu();
            return;
        }

        if (block instanceof ShulkerBox shulkerBox) {
            new ShulkerBoxMenu(player, shulkerBox).openMenu();
            return;
        }

        if (block instanceof Barrel barrel) {
            new BarrelMenu(player, barrel).openMenu();
        }
    }
}
