package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.EconomyData;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.math.BigDecimalUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.UUID;

public final class EconomyDataUtil {
    private static final ThreadLocal<Dao<EconomyData, UUID>> daoThread =
            ThreadLocal.withInitial(() -> {
                try {
                    return DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), EconomyData.class);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

    /**
     * 获取dao实例
     *
     * @return dao实例
     */
    public static Dao<EconomyData, UUID> getDao() {
        return daoThread.get();
    }

    /**
     * 检测指定玩家UUID是否存在经济数据实例
     *
     * @param uuid 玩家UUID
     * @return 结果
     */
    public static boolean ifEconomyDataExist(UUID uuid) {
        try {
            return getDao().queryForId(uuid) != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检测指定玩家实例是否存在经济数据实例
     *
     * @param player 玩家实例
     * @return 结果
     */
    public static boolean ifEconomyDataExist(OfflinePlayer player) {
        return ifEconomyDataExist(player.getUniqueId());
    }

    /**
     * 初始化指定玩家UUID经济数据实例
     *
     * @param uuid 玩家UUID
     */
    public static void initEconomyData(UUID uuid) {
        EconomyData economyData = new EconomyData();
        economyData.setPlayer(uuid);
        economyData.setBigDecimal(
                BigDecimalUtil.toBigDecimal(ConfigUtil.getConfig().getDouble("economySettings.default"))
        );

        updateEconomyData(economyData);
    }

    /**
     * 初始化指定玩家实例经济数据实例
     *
     * @param player 玩家实例
     */
    public static void initEconomyData(OfflinePlayer player) {
        initEconomyData(player.getUniqueId());
    }

    /**
     * 获取指定玩家UUID的经济数据实例
     *
     * @param uuid 玩家UUID
     * @return 经济数据实例
     */
    public static EconomyData getEconomyData(UUID uuid) {
        try {
            EconomyData economyData = getDao().queryForId(uuid);
            if (economyData == null) {
                economyData = new EconomyData();
                economyData.setPlayer(uuid);
                economyData.setBigDecimal(BigDecimalUtil.toBigDecimal(0.0));
            }

            return economyData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家实例的经济数据实例
     *
     * @param player 玩家实例
     * @return 经济数据实例
     */
    public static EconomyData getEconomyData(OfflinePlayer player) {
        return getEconomyData(player.getUniqueId());
    }

    /**
     * 更新指定经济数据实例在数据库中的数据
     *
     * @param economyData 经济数据实例
     */
    public static void updateEconomyData(EconomyData economyData) {
        try {
            getDao().createOrUpdate(economyData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}