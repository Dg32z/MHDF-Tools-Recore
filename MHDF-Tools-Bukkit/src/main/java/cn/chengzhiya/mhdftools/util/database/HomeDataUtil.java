package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.HomeData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class HomeDataUtil {
    private static final Dao<HomeData, Integer> homeDataDao;

    static {
        try {
            homeDataDao = DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), HomeData.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检测指定玩家UUID是否存在指定家名称的家
     *
     * @param uuid 玩家UUID
     * @param home 家名称
     * @return 结果
     */
    public static boolean ifHomeDataExist(UUID uuid, String home) {
        try {
            HomeData homeData = homeDataDao.queryBuilder()
                    .where()
                    .eq("player", uuid)
                    .and()
                    .eq("home", home)
                    .queryForFirst();

            return homeData != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检测指定玩家实例是否存在指定家名称的家
     *
     * @param player 玩家实例
     * @param home   家名称
     * @return 结果
     */
    public static boolean ifHomeDataExist(Player player, String home) {
        return ifHomeDataExist(player.getUniqueId(), home);
    }

    /**
     * 获取指定玩家UUID下指定家名称的家数据实例
     *
     * @param uuid 玩家UUID
     * @param home 家名称
     * @return 家数据实例
     */
    public static HomeData getHomeData(UUID uuid, String home) {
        try {
            HomeData homeData = homeDataDao.queryBuilder()
                    .where()
                    .eq("player", uuid)
                    .and()
                    .eq("home", home)
                    .queryForFirst();
            if (homeData == null) {
                homeData = new HomeData();
                homeData.setPlayer(uuid);
                homeData.setHome(home);
            }

            return homeData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家实例下指定家名称的家数据实例
     *
     * @param player 玩家实例
     * @param home   家名称
     * @return 家数据实例
     */
    public static HomeData getHomeData(Player player, String home) {
        return getHomeData(player.getUniqueId(), home);
    }

    /**
     * 获取指定玩家UUID的家数据实例列表
     *
     * @param uuid 玩家UUID
     * @return 家数据实例列表
     */
    public static List<HomeData> getHomeDataList(UUID uuid) {
        try {
            List<HomeData> homeDataList = homeDataDao.queryBuilder()
                    .where()
                    .eq("player", uuid)
                    .query();
            if (homeDataList == null) {
                homeDataList = new ArrayList<>();
            }

            return homeDataList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家实例的家数据实例列表
     *
     * @param player 玩家实例
     * @return 家数据实例列表
     */
    public static List<HomeData> getHomeDataList(Player player) {
        return getHomeDataList(player.getUniqueId());
    }

    /**
     * 更新指定家数据实例在数据库中的数据
     *
     * @param homeData 家数据实例
     */
    public static void updateHomeData(HomeData homeData) {
        try {
            homeDataDao.createOrUpdate(homeData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除指定家数据实例在数据库中的数据
     *
     * @param homeData 家数据实例
     */
    public static void removeHomeData(HomeData homeData) {
        try {
            homeDataDao.delete(homeData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
