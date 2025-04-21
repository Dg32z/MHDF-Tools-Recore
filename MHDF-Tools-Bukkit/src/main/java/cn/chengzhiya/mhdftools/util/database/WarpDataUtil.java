package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.WarpData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class WarpDataUtil {
    private static final Dao<WarpData, String> warpDataDao;

    static {
        try {
            warpDataDao = DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), WarpData.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检测是否存在指定传送点名称的传送点
     *
     * @param warp 传送点名称
     * @return 结果
     */
    public static boolean ifWarpDataExist(String warp) {
        try {
            WarpData warpData = warpDataDao.queryForId(warp);

            return warpData != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定传送点名称的传送点数据实例
     *
     * @param warp 传送点名称
     * @return 传送点数据实例
     */
    public static WarpData getWarpData(String warp) {
        try {
            WarpData warpData = warpDataDao.queryForId(warp);
            if (warpData == null) {
                warpData = new WarpData();
                warpData.setWarp(warp);
            }

            return warpData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取传送点数据实例列表
     *
     * @return 传送点数据实例列表
     */
    public static List<WarpData> getHomeDataList() {
        try {
            List<WarpData> warpDataList = warpDataDao.queryForAll();
            if (warpDataList == null) {
                warpDataList = new ArrayList<>();
            }

            return warpDataList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新指定传送点数据实例在数据库中的数据
     *
     * @param warpData 传送点数据实例
     */
    public static void updateWarpData(WarpData warpData) {
        try {
            warpDataDao.createOrUpdate(warpData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除指定传送点数据实例在数据库中的数据
     *
     * @param warpData 传送点数据实例
     */
    public static void removeWarpData(WarpData warpData) {
        try {
            warpDataDao.delete(warpData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
