package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.data.ChatIgnoreData;
import cn.chengzhiya.mhdftools.entity.data.FlyStatus;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public final class ChatIgnoreDataUtil {
    private static final Dao<ChatIgnoreData, Integer> chatIgnoreDataDao;

    static {
        try {
            chatIgnoreDataDao = DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), ChatIgnoreData.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家UUID的聊天屏蔽数据实例列表
     *
     * @param uuid 玩家UUID
     * @return 聊天屏蔽数据实例列表
     */
    public static List<ChatIgnoreData> getChatIgnoreDataList(UUID uuid) {
        try {
            return chatIgnoreDataDao.queryBuilder()
                    .where()
                    .eq("player", uuid)
                    .query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家实例的聊天屏蔽数据实例列表
     *
     * @param player 玩家实例
     * @return 聊天屏蔽数据实例列表
     */
    public static List<ChatIgnoreData> getChatIgnoreDataList(OfflinePlayer player) {
        return getChatIgnoreDataList(player.getUniqueId());
    }

    /**
     * 获取指定玩家UUID屏蔽指定玩家UUID的聊天屏蔽数据实例
     *
     * @param uuid 玩家UUID
     * @param ignoreUuid 被屏蔽的玩家UUID
     * @return 聊天屏蔽数据实例
     */
    public static ChatIgnoreData getChatIgnoreData(UUID uuid, UUID ignoreUuid) {
        try {
            return chatIgnoreDataDao.queryBuilder()
                    .where()
                    .eq("player", uuid)
                    .and()
                    .eq("ignore", ignoreUuid)
                    .queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家实例屏蔽指定玩家实例的聊天屏蔽数据实例
     *
     * @param player 玩家实例
     * @param ignorePlayer 被屏蔽的玩家实例
     * @return 聊天屏蔽数据实例
     */
    public static ChatIgnoreData getChatIgnoreData(OfflinePlayer player, OfflinePlayer ignorePlayer) {
        return getChatIgnoreData(player.getUniqueId(), ignorePlayer.getUniqueId());
    }

    /**
     * 判断指定玩家UUID是否屏蔽指定玩家UUID
     *
     * @param uuid 玩家UUID
     * @param ignoreUuid 被屏蔽的玩家UUID
     * @return 结果
     */
    public static boolean isChatIgnore(UUID uuid, UUID ignoreUuid) {
        return getChatIgnoreData(uuid, ignoreUuid) != null;
    }

    /**
     * 判断指定玩家实例是否屏蔽指定玩家实例
     *
     * @param player 玩家实例
     * @param ignorePlayer 被屏蔽的玩家实例
     * @return 结果
     */
    public static boolean isChatIgnore(OfflinePlayer player, OfflinePlayer ignorePlayer) {
        return isChatIgnore(player.getUniqueId(), ignorePlayer.getUniqueId());
    }

    /**
     * 移除指定聊天屏蔽数据实例在数据库中的数据
     *
     * @param chatIgnoreData 聊天屏蔽数据实例
     */
    public static void removeChatIgnoreData(ChatIgnoreData chatIgnoreData) {
        try {
            chatIgnoreDataDao.delete(chatIgnoreData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新指定聊天屏蔽数据实例在数据库中的数据
     *
     * @param chatIgnoreData 聊天屏蔽数据实例
     */
    public static void updateChatIgnoreData(ChatIgnoreData chatIgnoreData) {
        try {
            chatIgnoreDataDao.createOrUpdate(chatIgnoreData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
