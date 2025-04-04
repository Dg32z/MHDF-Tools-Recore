package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.random.RandomUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.BiomeSearchResult;

import java.util.ArrayList;
import java.util.List;

public final class RandomTeleportUtil {
    /**
     * 将指定玩家在指定世界实例随机传送
     *
     * @param player 玩家实例
     * @param world  世界实例
     * @param biome  群系实例
     */
    public static void randomTeleport(Player player, World world, Biome biome) {
        String group = getGroup(player);
        ConfigurationSection groupConfig = ConfigUtil.getConfig().getConfigurationSection("randomTeleportSettings." + group);
        if (groupConfig == null) {
            return;
        }

        int min = groupConfig.getInt("min");
        int max = groupConfig.getInt("max");

        List<String> blackBlock = groupConfig.getStringList("blackBlock");
        // 这并不会导致性能问题,只有特别频繁的调用才有可能出问题,不要杞人忧天 ):
        int centerX = RandomUtil.randomInt(min, max);
        int centerZ = RandomUtil.randomInt(min, max);

        Location centerLocation = new Location(world, centerX, 60, centerZ);

        if (biome != null) {
            BiomeSearchResult result = world.locateNearestBiome(new Location(world, centerX, 60, centerZ), max, 64, 64, biome);
            if (result == null) {
                return;
            }
            centerLocation = result.getLocation();
        }

        for (int y = world.getMaxHeight(); y > 60; y--) {
            Location location = centerLocation.clone();
            location.setY(y);

            Block block = location.getBlock();
            if (block.getType() == Material.AIR) {
                continue;
            }
            if (blackBlock.contains(block.getType().toString())) {
                randomTeleport(player, world, biome);
                return;
            }
            location.setY(location.getY() + 1);
            player.teleportAsync(location);
            return;
        }
    }

    /**
     * 将指定玩家在指定世界实例随机传送
     *
     * @param player 玩家实例
     * @param world  世界实例
     */
    public static void randomTeleport(Player player, World world) {
        randomTeleport(player, world, null);
    }

    /**
     * 将指定玩家在当前世界实例随机传送
     *
     * @param player 玩家实例
     */
    public static void randomTeleport(Player player) {
        randomTeleport(player, player.getWorld());
    }

    /**
     * 获取指定玩家所在的组
     *
     * @param player 玩家实例
     * @return 组名称
     */
    private static String getGroup(Player player) {
        List<String> groupList = new ArrayList<>();
        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            String permission = permissionAttachmentInfo.getPermission();
            if (permission.startsWith("mhdftools.group.randomteleport.")) {
                String replace = permission.replace("mhdftools.group.randomteleport.", "");
                groupList.add(replace);
            }
        }

        int maxWeight = 0;
        String maxWeightGroup = "default";

        for (String group : groupList) {
            int weight = ConfigUtil.getConfig().getInt("randomTeleportSettings." + group + ".weight");

            if (weight > maxWeight) {
                maxWeight = weight;
                maxWeightGroup = group;
            }
        }

        return maxWeightGroup;
    }
}
