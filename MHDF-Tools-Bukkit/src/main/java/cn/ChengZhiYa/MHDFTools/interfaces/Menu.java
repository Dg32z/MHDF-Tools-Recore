package cn.ChengZhiYa.MHDFTools.interfaces;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface Menu {
    void onClick(InventoryClickEvent event);

    void onClose(InventoryCloseEvent event);
}
