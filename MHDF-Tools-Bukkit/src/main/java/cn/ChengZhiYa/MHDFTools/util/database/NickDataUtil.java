package cn.ChengZhiYa.MHDFTools.util.database;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.entity.data.NickData;
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

    public static NickData getNickData(OfflinePlayer player) {
        return getNickData(player.getUniqueId());
    }

    public static void updateNickData(NickData nickData) {
        try {
            nickDataDao.createIfNotExists(nickData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
