package cn.ChengZhiYa.MHDFTools.listener.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

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

            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            BlockStateMeta blockMate = (BlockStateMeta) meta;
            org.bukkit.block.ShulkerBox box = (org.bukkit.block.ShulkerBox) blockMate.getBlockState();
            Inventory inv = Bukkit.createInventory(player, InventoryType.SHULKER_BOX, LangUtil.i18n("fastuse.shulkerBox"));
            inv.setContents(box.getInventory().getContents());
            player.openInventory(inv);

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

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!ConfigUtil.getConfig().getBoolean("fastUseSettings.shulkerBox")) {
            return;
        }

        if (event.getView().getOriginalTitle().equals(LangUtil.i18n("fastuse.shulkerBox"))) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack mainItem = player.getInventory().getItemInMainHand();

        if (!mainItem.getType().toString().contains("SHULKER_BOX")) {
            event.setCancelled(true);
            player.closeInventory();
            return;
        }

        ItemStack clickItem = event.getCurrentItem();

        if (event.getClick() == ClickType.NUMBER_KEY) {
            clickItem = player.getInventory().getItem(event.getHotbarButton());
        }

        if (clickItem == null) {
            return;
        }

        if (clickItem.getType().toString().contains("SHULKER_BOX")) {
            event.setCancelled(true);
            return;
        }

        updateShulker(player, event.getInventory());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!ConfigUtil.getConfig().getBoolean("fastUseSettings.shulkerBox")) {
            return;
        }

        if (event.getView().getOriginalTitle().equals(LangUtil.i18n("fastuse.shulkerBox"))) {
            return;
        }

        Player player = (Player) event.getPlayer();
        ItemStack mainItem = player.getInventory().getItemInMainHand();

        if (!mainItem.getType().toString().contains("SHULKER_BOX")) {
            player.closeInventory();
            return;
        }

        updateShulker(player, event.getInventory());
    }

    /**
     * 更新手中潜影盒
     *
     * @param player    玩家实例
     * @param inventory 潜影盒背包实例
     */
    private void updateShulker(Player player, Inventory inventory) {
        ItemStack mainItem = player.getInventory().getItemInMainHand();

        if (mainItem.getItemMeta() instanceof BlockStateMeta blockStateMeta) {
            ShulkerBox box = (ShulkerBox) blockStateMeta.getBlockState();

            box.getInventory().setContents(inventory.getContents());

            blockStateMeta.setBlockState(box);
            mainItem.setItemMeta(blockStateMeta);
            player.getInventory().setItemInMainHand(mainItem);
        }
    }
}
