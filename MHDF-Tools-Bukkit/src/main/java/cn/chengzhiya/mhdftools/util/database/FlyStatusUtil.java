package cn.ChengZhiYa.MHDFTools.util.database;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.entity.data.FlyStatus;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.UUID;

public final class FlyStatusUtil {
    private static final Dao<FlyStatus, UUID> flyStatusDao;

    static {
        try {
            flyStatusDao = DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), FlyStatus.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家UUID的飞行状态实例
     *
     * @param uuid 玩家UUID
     * @return 飞行状态实例
     */
    public static FlyStatus getFlyStatus(UUID uuid) {
        try {
            FlyStatus flyStatus = flyStatusDao.queryForId(uuid);
            if (flyStatus == null) {
                flyStatus = new FlyStatus();
                flyStatus.setPlayer(uuid);
            }

            return flyStatus;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家实例的飞行状态实例
     *
     * @param player 玩家实例
     * @return 飞行状态实例
     */
    public static FlyStatus getFlyStatus(OfflinePlayer player) {
        return getFlyStatus(player.getUniqueId());
    }

    /**
     * 更新指定飞行状态实例在数据库中的数据
     *
     * @param flyStatus 飞行状态实例
     */
    public static void updateFlyStatus(FlyStatus flyStatus) {
        try {
            flyStatusDao.createOrUpdate(flyStatus);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
