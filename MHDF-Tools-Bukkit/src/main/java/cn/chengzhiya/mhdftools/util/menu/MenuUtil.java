package cn.ChengZhiYa.MHDFTools.util.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class MenuUtil {
    /**
     * 获取指定菜单点击事件中点击的物品实例
     *
     * @param event 菜单点击事件实例
     * @return 物品实例
     */
    public static ItemStack getClickItem(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return null;
        }

        Player player = (Player) event.getWhoClicked();

        if (event.getClick() == ClickType.NUMBER_KEY) {
            ItemStack item = player.getInventory().getItem(event.getHotbarButton());
            if (item == null) {
                return event.getClickedInventory().getItem(event.getRawSlot());
            }
            return item;
        }

        return event.getCurrentItem();
    }

    /**
     * 获取格子列表
     *
     * @param slot 格子文本
     * @return 格子列表
     */
    public static List<Integer> getSlotList(String slot) {
        if (slot == null) {
            return new ArrayList<>();
        }

        List<Integer> slotList = new ArrayList<>();

        if (slot.contains("-")) {
            String[] data = slot.split("-");
            int start = Integer.parseInt(data[0]);
            int end = Integer.parseInt(data[1]);
            for (int i = start; i < end; i++) {
                slotList.add(i);
            }
        } else {
            slotList.add(Integer.parseInt(slot));
        }

        return slotList;
    }

    /**
     * 获取格子列表
     *
     * @param slots 格子文本列表
     * @return 格子列表
     */
    public static List<Integer> getSlotList(List<String> slots) {
        List<Integer> slotList = new ArrayList<>();

        for (String slot : slots) {
            slotList.addAll(getSlotList(slot));
        }

        return slotList;
    }
}
