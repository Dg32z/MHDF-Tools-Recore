package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public final class HomeUtil {
    /**
     * 获取指定玩家实例的家数量
     *
     * @param player 玩家实例
     * @return 家数量
     */
    public static int getMaxHome(Player player) {
        for (PermissionAttachmentInfo permInfo : player.getEffectivePermissions()) {
            String perm = permInfo.getPermission();
            if (perm.startsWith("mhdftools.commands.home.max.")) {
                try {
                    return Integer.parseInt(perm.substring("mhdftools.commands.home.max.".length()));
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return ConfigUtil.getConfig().getInt("homeSettings.defaultMax");
    }
}
