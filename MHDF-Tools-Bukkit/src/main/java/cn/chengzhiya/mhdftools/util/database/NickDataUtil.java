package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.data.NickData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.UUID;

public final class NickDataUtil {
    private static final Dao<NickData, UUID> nickDataDao;

    static {
        try {
            nickDataDao = DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), NickData.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家UUID的匿名数据实例
     *
     * @param uuid 玩家UUID
     * @return 匿名数据实例
     */
    public static NickData getNickData(UUID uuid) {
        try {
            NickData nickData = nickDataDao.queryForId(uuid);
            if (nickData == null) {
                nickData = new NickData();
                nickData.setPlayer(uuid);
            }

            return nickData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家实例的匿名数据实例
     *
     * @param player 玩家实例
     * @return 匿名数据实例
     */
    public static NickData getNickData(OfflinePlayer player) {
        return getNickData(player.getUniqueId());
    }

    /**
     * 更新指定匿名数据实例在数据库中的数据
     *
     * @param nickData 匿名数据实例
     */
    public static void updateNickData(NickData nickData) {
        try {
            nickDataDao.createOrUpdate(nickData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
