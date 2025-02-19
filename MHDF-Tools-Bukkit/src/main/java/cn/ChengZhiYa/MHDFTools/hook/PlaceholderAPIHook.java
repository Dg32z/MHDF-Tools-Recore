package cn.ChengZhiYa.MHDFTools.hook;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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

    /**
     * 处理PAPI变量
     *
     * @param player  玩家实例
     * @param message 要处理的文本
     * @return 处理过后的文本
     */
    public String placeholder(OfflinePlayer player, String message) {
        if (isEnable()) {
            return PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }
}
