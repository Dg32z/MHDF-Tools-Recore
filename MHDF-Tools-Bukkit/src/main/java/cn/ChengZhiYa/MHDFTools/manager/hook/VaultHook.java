package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.manager.AbstractHook;
import org.bukkit.Bukkit;

public final class VaultHook extends AbstractHook {
    /**
     * 初始化Vault的API
     */
    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            super.enable = true;
        }
    }

    /**
     * 卸载Vault的API
     */
    @Override
    public void unhook() {
        super.enable = false;
    }
}
