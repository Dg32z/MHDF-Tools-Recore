package cn.chengzhiya.mhdftools.interfaces;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface Menu {
    void click(InventoryClickEvent event);

    void close(InventoryCloseEvent event);
}
