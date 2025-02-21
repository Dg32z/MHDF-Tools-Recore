package cn.ChengZhiYa.MHDFTools.hook.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public final class PlaceholderApiImpl {
    public PlaceholderApiImpl() {

    }

    /**
     * 处理PAPI变量
     *
     * @param player  玩家实例
     * @param message 要处理的文本
     * @return 处理过后的文本
     */
    public String placeholder(OfflinePlayer player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }
}
