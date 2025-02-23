package cn.chengzhiya.mhdftools.util.menu;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ItemStackUtil {
    /**
     * 构建物品实例
     *
     * @param player          玩家实例
     * @param type            类型
     * @param name            名称
     * @param lore            简介列表
     * @param amount          数量
     * @param customModelData 自定义模型数据
     * @return 物品实例
     */
    public static ItemStack getItemStack(Player player, String type, String name, List<String> lore, Integer amount, Integer customModelData) {
        if (type == null) {
            return new ItemStack(Material.AIR);
        }

        ItemStack item;
        if (type.startsWith("craftEngine-")) {
            item = Main.instance.getPluginHookManager().getCraftEngineHook().getItem(
                    type.replace("craftEngine-", ""),
                    player
            );
        } else if (type.startsWith("mythicMobs-")) {
            item = Main.instance.getPluginHookManager().getMythicMobsHook().getItem(
                    type.replace("mythicMobs-", "")
            );
        } else if (type.startsWith("head-")) {
            item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            String playerName = type.replace("head-", "");
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));

            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(type)));
        }

        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.setDisplayName(ColorUtil.color(Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, name)));
        }

        if (lore != null && !lore.isEmpty()) {
            meta.setLore(lore.stream()
                    .map(s -> Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, s))
                    .map(ColorUtil::color)
                    .toList()
            );
        }

        if (amount != null && amount > 0) {
            item.setAmount(amount);
        }

        if (customModelData != null && customModelData > 0) {
            meta.setCustomModelData(customModelData);
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * 构建物品实例
     *
     * @param player 玩家实例
     * @param type   类型
     * @param name   名称
     * @param lore   简介列表
     * @param amount 数量
     * @return 物品实例
     */
    public static ItemStack getItemStack(Player player, String type, String name, List<String> lore, Integer amount) {
        return getItemStack(player, type, name, lore, amount, null);
    }

    /**
     * 构建物品实例
     *
     * @param player 玩家实例
     * @param type   类型
     * @param name   名称
     * @param lore   简介列表
     * @return 物品实例
     */
    public static ItemStack getItemStack(Player player, String type, String name, List<String> lore) {
        return getItemStack(player, type, name, lore, 1, null);
    }

    /**
     * 构建物品实例
     *
     * @param player 玩家实例
     * @param type   类型
     * @param name   名称
     * @return 物品实例
     */
    public static ItemStack getItemStack(Player player, String type, String name) {
        return getItemStack(player, type, name, new ArrayList<>(), 1, null);
    }

    /**
     * 构建物品实例
     *
     * @param player 玩家实例
     * @param type   类型
     * @return 物品实例
     */
    public static ItemStack getItemStack(Player player, String type) {
        return getItemStack(player, type, null, new ArrayList<>(), 1, null);
    }
}
