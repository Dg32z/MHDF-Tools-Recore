package cn.chengzhiya.mhdftools.util.menu;

import cn.chengzhiya.mhdftools.builder.ItemStackBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public final class ItemStackUtil {
    /**
     * 获取指定物品配置实例对应的物品构建实例
     *
     * @param player 玩家实例
     * @param item   物品配置实例
     * @return 物品构建实例
     */
    public static ItemStackBuilder getItemStackBuilder(Player player, ConfigurationSection item) {
        return new ItemStackBuilder(player, item.getString("type"))
                .name(item.getString("name"))
                .lore(item.getStringList("lore"))
                .amount(item.getInt("amount"))
                .customModelData(item.getInt("customModelData"));
    }
}
