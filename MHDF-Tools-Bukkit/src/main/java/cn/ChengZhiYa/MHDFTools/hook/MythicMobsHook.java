package cn.ChengZhiYa.MHDFTools.hook;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

@Getter
public final class MythicMobsHook extends AbstractHook {
    private MythicBukkit api;

    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            this.api = MythicBukkit.inst();
            super.enable = true;
        }
    }

    @Override
    public void unhook() {
        this.api = null;
        super.enable = false;
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
