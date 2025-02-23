package cn.ChengZhiYa.MHDFTools.hook.impl;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public final class MythicMobsImpl {
    private final MythicBukkit api;

    public MythicMobsImpl() {
        this.api = MythicBukkit.inst();
    }

    /**
     * 获取指定物品ID的物品实例
     *
     * @param id 物品ID
     * @return 物品实例
     */
    public ItemStack getItem(String id) {
        return getApi().getItemManager().getItemStack(id);
    }
}
