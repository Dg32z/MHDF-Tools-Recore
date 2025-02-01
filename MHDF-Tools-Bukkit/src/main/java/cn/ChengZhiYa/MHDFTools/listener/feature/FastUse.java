package cn.ChengZhiYa.MHDFTools.listener.feature;

import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.menu.fastuse.ShulkerBoxMenu;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class FastUse extends AbstractListener {
    public FastUse() {
        super(
                "fastUseSettings.enable"
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        // 潜影盒
        if (item.getType().toString().contains("SHULKER_BOX")) {
            if (!ConfigUtil.getConfig().getBoolean("fastUseSettings.shulkerBox")) {
                return;
            }

            new ShulkerBoxMenu(player).openMenu();
            return;
        }

        // 末影箱
        if (item.getType() == Material.ENDER_CHEST) {
            if (!ConfigUtil.getConfig().getBoolean("fastUseSettings.enderChest")) {
                return;
            }

            player.openInventory(player.getEnderChest());
            return;
        }

        // 工作台
        if (item.getType() == Material.CRAFTING_TABLE) {
            if (!ConfigUtil.getConfig().getBoolean("fastUseSettings.craftingTable")) {
                return;
            }

            player.openWorkbench(null, true);
        }
    }
}
