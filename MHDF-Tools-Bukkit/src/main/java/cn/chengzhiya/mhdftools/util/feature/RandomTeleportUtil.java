package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.BiomeSearchResult;

import java.util.List;
import java.util.Random;

public final class RandomTeleportUtil {
    /**
     * 将指定玩家在指定世界实例随机传送
     *
     * @param player 玩家实例
     * @param world  世界实例
     * @param biome  群戏实例
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
        int centerX = new Random().nextInt(min, max);
        int centerZ = new Random().nextInt(min, max);

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
            player.teleport(location);
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
        List<String> groupList = player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("mhdftools.group.randomteleport."))
                .map(permission -> permission.replace("mhdftools.group.randomteleport.", ""))
                .toList();

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
