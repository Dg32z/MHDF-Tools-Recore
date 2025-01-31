package cn.ChengZhiYa.MHDFTools.util.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.entity.data.VanishStatus;
import cn.ChengZhiYa.MHDFTools.manager.init.PluginHookManager;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

@SuppressWarnings("unused")
public final class VanishUtil {
    private static final Dao<VanishStatus, UUID> vanishStatusDao;

    static {
        try {
            vanishStatusDao = DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), VanishStatus.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家UUID的影身状态实例
     *
     * @param uuid 玩家UUID
     * @return 影身状态实例
     */
    public static VanishStatus getVanishStatus(UUID uuid) {
        try {
            VanishStatus vanishStatus = vanishStatusDao.queryForId(uuid);
            if (vanishStatus == null) {
                vanishStatus = new VanishStatus();
                vanishStatus.setPlayer(uuid);
            }
            return vanishStatus;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家实例的影身状态实例
     *
     * @param player 玩家实例
     * @return 影身状态实例
     */
    public static VanishStatus getVanishStatus(Player player) {
        return getVanishStatus(player.getUniqueId());
    }

    /**
     * 设置指定玩家的影身状态实例
     *
     * @param vanishStatus 影身状态实例
     */
    public static void setVanishStatus(VanishStatus vanishStatus) {
        try {
            vanishStatusDao.createOrUpdate(vanishStatus);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除指定玩家UUID的影身状态实例
     *
     * @param uuid 玩家UUID
     */
    public static void deleteVanishStatus(UUID uuid) {
        try {
            vanishStatusDao.deleteById(uuid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除指定玩家实例的影身状态实例
     *
     * @param player 玩家实例
     */
    public static void deleteVanishStatus(Player player) {
        deleteVanishStatus(player.getUniqueId());
    }

    /**
     * 给指定目标实例发送切换影身的提示
     *
     * @param sender 接收信息的目标实例
     * @param player 开启影身的玩家实例
     * @param enable 是否开启影身
     */
    public static void sendChangeVanishMessage(CommandSender sender, Player player, boolean enable) {
        sender.sendMessage(
                LangUtil.i18n("commands.vanish.message")
                        .replace("{player}", player.getName())
                        .replace("{change}",
                                enable ? LangUtil.i18n("enable") : LangUtil.i18n("disable")
                        )
        );
    }

    /**
     * 开启指定玩家实例的影身模式
     *
     * @param player 玩家实例
     */
    public static void enableVanish(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (PluginHookManager.getPacketEventsHook().getServerManager().getVersion()
                    .isNewerThanOrEquals(ServerVersion.V_1_12_2)) {
                onlinePlayer.hidePlayer(Main.instance, player);
            } else {
                onlinePlayer.hidePlayer(player);
            }
        }
    }

    /**
     * 关闭指定玩家实例的影身模式
     *
     * @param player 玩家实例
     */
    public static void disableVanish(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (PluginHookManager.getPacketEventsHook().getServerManager().getVersion()
                    .isNewerThanOrEquals(ServerVersion.V_1_12_2)) {
                onlinePlayer.showPlayer(Main.instance, player);
            } else {
                onlinePlayer.showPlayer(player);
            }
        }
    }
}
