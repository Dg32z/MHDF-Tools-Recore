package cn.ChengZhiYa.MHDFTools.util.feature;

import cn.ChengZhiYa.MHDFTools.entity.data.NickData;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.database.NickDataUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class NickUtil {
    /**
     * 获取指定玩家UUID的匿名昵称
     *
     * @param uuid 玩家UUID
     * @return 匿名昵称
     */
    public static String getNickName(UUID uuid) {
        NickData nickData = NickDataUtil.getNickData(uuid);

        return nickData.getNick();
    }

    /**
     * 获取指定玩家实例的匿名昵称
     *
     * @param player 玩家实例
     * @return 匿名昵称
     */
    public static String getNickName(OfflinePlayer player) {
        return getNickName(player.getUniqueId());
    }

    /**
     * 获取指定玩家实例的游戏昵称
     *
     * @param player 玩家实例
     * @return 游戏昵称
     */
    public static String getName(OfflinePlayer player) {
        // 不处理功能未开启的情况
        if (ConfigUtil.getConfig().getBoolean("nickSettings.enable")) {
            return player.getName();
        }

        String nickName = getNickName(player);
        if (nickName == null) {
            return player.getName();
        }
        return nickName;
    }

    /**
     * 设置指定玩家实例的匿名昵称
     *
     * @param player 玩家实例
     * @param name   匿名昵称
     */
    public static void setNickName(Player player, String name) {
        NickData nickData = NickDataUtil.getNickData(player);
        nickData.setNick(name);
        NickDataUtil.updateNickData(nickData);

        setNickName(player, name);
    }

    /**
     * 设置指定玩家实例的匿名昵称显示
     *
     * @param player 玩家实例
     * @param name   匿名昵称
     */
    public static void setNickDisplay(Player player, String name) {
        player.setDisplayName(name);
        player.setCustomName(name);
        player.setPlayerListName(name);
        player.setCustomNameVisible(true);
    }
}
