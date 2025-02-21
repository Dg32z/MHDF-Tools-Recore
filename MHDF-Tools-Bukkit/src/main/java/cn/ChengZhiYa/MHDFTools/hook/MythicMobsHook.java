package cn.ChengZhiYa.MHDFTools.hook;

import cn.ChengZhiYa.MHDFTools.hook.impl.MythicMobsImpl;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public final class MythicMobsHook extends AbstractHook {
    private MythicMobsImpl api;

    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            this.api = new MythicMobsImpl();
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
        if (isEnable()) {
            return getApi().getItem(id);
        }
        return new ItemStack(Material.AIR);
    }
}
