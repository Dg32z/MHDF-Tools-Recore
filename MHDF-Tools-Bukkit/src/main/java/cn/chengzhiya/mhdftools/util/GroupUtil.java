package cn.chengzhiya.mhdftools.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

public final class GroupUtil {
    /**
     * 获取指定玩家所在的组
     *
     * @param player           玩家实例
     * @param config           配置实例
     * @param permissionPrefix 权限前缀
     * @return 组名称
     */
    public static String getGroup(Player player, ConfigurationSection config, String permissionPrefix) {
        if (config == null || permissionPrefix == null) {
            return "default";
        }

        List<String> groupList = player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith(permissionPrefix))
                .map(permission -> permission.replace(permissionPrefix, ""))
                .toList();

        int maxWeight = 0;
        String maxWeightGroup = "default";

        for (String group : groupList) {
            int weight = config.getInt(group + ".weight");

            if (weight > maxWeight) {
                maxWeight = weight;
                maxWeightGroup = group;
            }
        }

        return maxWeightGroup;
    }
}
