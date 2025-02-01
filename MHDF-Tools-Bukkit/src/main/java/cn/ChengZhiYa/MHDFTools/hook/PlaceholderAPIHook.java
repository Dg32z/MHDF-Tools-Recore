package cn.ChengZhiYa.MHDFTools.hook;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
public final class PlaceholderAPIHook extends AbstractHook {
    /**
     * 初始化PlaceholderAPI的API
     */
    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            super.enable = true;
        }
    }

    /**
     * 卸载PlaceholderAPI的API
     */
    @Override
    public void unhook() {
        super.enable = false;
    }
}
