package cn.chengzhiya.mhdftools.util.menu;

import cn.chengzhiya.mhdftools.builder.ItemStackBuilder;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.action.RequirementUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class MenuUtil {
    /**
     * 构建物品实例
     *
     * @param player 玩家实例
     * @param config 配置实例
     * @return 物品实例
     */
    public static ItemStack getItemStack(Player player, ConfigurationSection config) {
        return new ItemStackBuilder(config.getString("type"))
                .name(config.getString("name"))
                .lore(config.getStringList("lore"))
                .amount(config.getInt("amount"))
                .customModelData(config.getInt("customModelData"))
                .build(player);
    }

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

    /**
     * 获取格子列表
     *
     * @param config 配置实例
     * @return 格子列表
     */
    public static List<Integer> getSlotList(ConfigurationSection config) {
        List<Integer> slotList = new ArrayList<>();
        if (!config.getStringList("slots").isEmpty()) {
            slotList.addAll(getSlotList(config.getStringList("slots")));
        } else {
            slotList.addAll(getSlotList(config.getString("slot")));
        }

        return slotList;
    }

    /**
     * 为指定玩家实例执行指定物品配置实例的操作
     *
     * @param player 玩家实例
     * @param item   物品配置实例
     */
    public static void runItemClickAction(Player player, ConfigurationSection item) {
        if (item == null) {
            return;
        }

        ConfigurationSection clickRequirementsConfig = item.getConfigurationSection("clickRequirements");
        if (clickRequirementsConfig != null) {
            List<String> denyAction = RequirementUtil.checkRequirements(player, clickRequirementsConfig);
            if (!denyAction.isEmpty()) {
                ActionUtil.runActionList(player, denyAction);
                return;
            }
        }

        List<String> clickAction = item.getStringList("clickAction");
        if (!clickAction.isEmpty()) {
            ActionUtil.runActionList(player, clickAction);
        }
    }

    /**
     * 为指定玩家实例执行指定菜单配置实例下指定物品ID的操作
     *
     * @param player 玩家实例
     * @param menu   菜单配置实例
     * @param key    物品ID
     */
    public static void runItemClickAction(Player player, ConfigurationSection menu, String key) {
        if (menu == null) {
            return;
        }

        ConfigurationSection items = menu.getConfigurationSection("items");
        if (items == null) {
            return;
        }

        runItemClickAction(player, items.getConfigurationSection(key));
    }
}
