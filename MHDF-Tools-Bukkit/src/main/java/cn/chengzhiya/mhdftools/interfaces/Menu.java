package cn.chengzhiya.mhdftools.interfaces;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface Menu {
    void open(InventoryOpenEvent event);

    void click(InventoryClickEvent event);

    void close(InventoryCloseEvent event);
}
