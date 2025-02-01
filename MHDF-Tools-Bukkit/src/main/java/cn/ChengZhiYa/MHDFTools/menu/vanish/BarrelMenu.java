package cn.ChengZhiYa.MHDFTools.menu.vanish;

import cn.ChengZhiYa.MHDFTools.menu.AbstractMenu;
import cn.ChengZhiYa.MHDFTools.util.message.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class BarrelMenu extends AbstractMenu {
    private final Barrel barrel;

    public BarrelMenu(Player player, Barrel barrel) {
        super(
                "vanishSettings.enable",
                player
        );
        this.barrel = barrel;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(getPlayer(), InventoryType.BARREL, ColorUtil.color("menu.vanish.title"));
        inventory.setContents(barrel.getInventory().getContents());

        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        saveInventory(event.getInventory());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        saveInventory(event.getInventory());
    }

    private void saveInventory(Inventory inventory) {
        barrel.getInventory().setContents(inventory.getContents());
    }
}
