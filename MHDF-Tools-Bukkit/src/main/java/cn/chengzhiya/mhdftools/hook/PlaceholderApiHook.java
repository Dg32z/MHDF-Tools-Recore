package cn.chengzhiya.mhdftools.hook;

import cn.chengzhiya.mhdftools.hook.impl.PlaceholderApiImpl;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

@Getter
public final class PlaceholderApiHook extends AbstractHook {
    private PlaceholderApiImpl api;

    /**
     * 初始化PlaceholderAPI的API
     */
    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.api = new PlaceholderApiImpl();
            super.enable = true;
        }
    }

    /**
     * 卸载PlaceholderAPI的API
     */
    @Override
    public void unhook() {
        this.api = null;
        super.enable = false;
    }

    /**
     * 处理PAPI变量
     *
     * @param player  玩家实例
     * @param message 要处理的文本
     * @return 处理过后的文本
     */
    public String placeholder(OfflinePlayer player, String message) {
        if (isEnable()) {
            return getApi().placeholder(player, message);
        }
        return message;
    }
}
