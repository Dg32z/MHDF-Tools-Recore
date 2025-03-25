package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.entity.Player;

public final class TpaHereUtil {
    /**
     * 使指定玩家实例给指定玩家ID的玩家发送tpahere传送请求
     *
     * @param player     玩家实例
     * @param targetName 玩家ID
     */
    public static void sendTpaHereRequest(Player player, String targetName) {
        if (!Main.instance.getBungeeCordManager().ifPlayerOnline(targetName)) {
            ActionUtil.sendMessage(player, LangUtil.i18n("playerOffline"));
            return;
        }

        if (targetName.equals(player.getName())) {
            ActionUtil.sendMessage(player, LangUtil.i18n("commands.tpahere.sendSelf"));
            return;
        }

        Main.instance.getCacheManager().put(player.getName() + "_tpaherePlayer", targetName);
        Main.instance.getCacheManager().put(player.getName() + "_tpahereDelay", String.valueOf(ConfigUtil.getConfig().getInt("tpahereSettings.delay")));

        Main.instance.getBungeeCordManager().sendMessage(targetName, LangUtil.i18n("commands.tpahere.requestMessage")
                .replace("{player}", player.getName())
        );

        ActionUtil.sendMessage(player, LangUtil.i18n("commands.tpahere.message")
                .replace("{player}", targetName)
        );
    }
}
