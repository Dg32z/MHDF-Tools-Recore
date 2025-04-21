package cn.chengzhiya.mhdftools.hook;

import cn.chengzhiya.mhdftools.hook.impl.VaultImpl;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public final class VaultHook extends AbstractHook {
    private VaultImpl api;

    /**
     * 初始化Vault的API
     */
    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            this.api = new VaultImpl();
            super.enable = true;
        }
    }

    /**
     * 卸载Vault的API
     */
    @Override
    public void unhook() {
        if (this.api != null) {
            getApi().unhook();
        }
        this.api = null;
        super.enable = false;
    }
}
