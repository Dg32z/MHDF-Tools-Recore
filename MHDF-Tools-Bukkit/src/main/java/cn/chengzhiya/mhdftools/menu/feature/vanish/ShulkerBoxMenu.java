package cn.chengzhiya.mhdftools.menu.feature.vanish;

import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class ShulkerBoxMenu extends AbstractMenu {
    private final ShulkerBox shulkerBox;

    public ShulkerBoxMenu(Player player, ShulkerBox shulkerBox) {
        super(
                "vanishSettings.enable",
                player
        );
        this.shulkerBox = shulkerBox;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, InventoryType.SHULKER_BOX, LangUtil.i18n("menu.vanish.title"));
        inventory.setContents(shulkerBox.getInventory().getContents());

        return inventory;
    }

    @Override
    public void click(InventoryClickEvent event) {
        saveInventory(event.getInventory());
    }

    @Override
    public void close(InventoryCloseEvent event) {
        saveInventory(event.getInventory());
    }

    private void saveInventory(Inventory inventory) {
        shulkerBox.getInventory().setContents(inventory.getContents());
    }
}
