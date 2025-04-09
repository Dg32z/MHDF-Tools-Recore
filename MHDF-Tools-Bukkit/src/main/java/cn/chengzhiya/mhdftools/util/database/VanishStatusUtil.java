package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.data.VanishStatus;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public final class VanishStatusUtil {
    private static final Dao<VanishStatus, UUID> vanishStatusDao;

    static {
        try {
            vanishStatusDao = DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), VanishStatus.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取隐身状态实例列表
     *
     * @return 隐身状态实例列表
     */
    public static List<VanishStatus> getVanishStatusList() {
        try {
            return vanishStatusDao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家UUID的隐身状态实例
     *
     * @param uuid 玩家UUID
     * @return 隐身状态实例
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
     * 获取指定玩家实例的隐身状态实例
     *
     * @param player 玩家实例
     * @return 隐身状态实例
     */
    public static VanishStatus getVanishStatus(OfflinePlayer player) {
        return getVanishStatus(player.getUniqueId());
    }

    /**
     * 更新指定隐身状态实例在数据库中的数据
     *
     * @param vanishStatus 隐身状态实例
     */
    public static void updateVanishStatus(VanishStatus vanishStatus) {
        try {
            vanishStatusDao.createOrUpdate(vanishStatus);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
