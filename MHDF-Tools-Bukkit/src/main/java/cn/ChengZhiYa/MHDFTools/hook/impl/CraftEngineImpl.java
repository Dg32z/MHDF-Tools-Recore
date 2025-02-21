package cn.ChengZhiYa.MHDFTools.hook.impl;

import lombok.Getter;
import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public final class CraftEngineImpl {
    private final CraftEngine api;
    private final BukkitCraftEngine bukkitApi;

    public CraftEngineImpl() {
        this.api = CraftEngine.instance();
        this.bukkitApi = BukkitCraftEngine.instance();
    }

    /**
     * 转换玩家实例
     *
     * @param player bukkit玩家实例
     * @return craftEngine玩家实例
     */
    public net.momirealms.craftengine.core.entity.player.Player adaptPlayer(Player player) {
        return getBukkitApi().adapt(player);
    }

    /**
     * 获取指定物品ID的物品实例
     *
     * @param item   物品ID
     * @param player craftEngine玩家实例
     * @return 物品实例
     */
    public ItemStack getItem(String item, net.momirealms.craftengine.core.entity.player.Player player) {
        return (ItemStack) getApi().itemManager().buildCustomItemStack(Key.of(item), player);
    }

    /**
     * 获取指定物品ID的物品实例
     *
     * @param item   物品ID
     * @param player bukkit玩家实例
     * @return 物品实例
     */
    public ItemStack getItem(String item, Player player) {
        return getItem(item, adaptPlayer(player));
    }
}
