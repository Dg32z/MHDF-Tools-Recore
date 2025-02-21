package cn.ChengZhiYa.MHDFTools.hook;

import cn.ChengZhiYa.MHDFTools.hook.impl.CraftEngineImpl;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public final class CraftEngineHook extends AbstractHook {
    private CraftEngineImpl api;

    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("CraftEngine") != null) {
            this.api = new CraftEngineImpl();
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
     * @param item   物品ID
     * @param player bukkit玩家实例
     * @return 物品实例
     */
    public ItemStack getItem(String item, Player player) {
        if (isEnable()) {
            return getApi().getItem(item, player);
        }
        return new ItemStack(Material.AIR);
    }
}
