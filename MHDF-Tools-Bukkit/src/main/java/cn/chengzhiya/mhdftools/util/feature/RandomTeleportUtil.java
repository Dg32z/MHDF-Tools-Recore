package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.util.GroupUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.math.RandomUtil;
import cn.chengzhiya.mhdftools.util.teleport.TeleportUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.BiomeSearchResult;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class RandomTeleportUtil {
    /**
     * 获取指定玩家实例的组配置实例
     *
     * @param player 玩家实例
     * @return 组配置实例
     */
    public static ConfigurationSection getGroupConfigurationSection(Player player) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("randomTeleportSettings");
        if (config == null) {
            return null;
        }

        String groupId = GroupUtil.getGroup(player, config, "mhdftools.group.randomteleport.");

        return config.getConfigurationSection(groupId);
    }

    /**
     * 将指定玩家在指定世界实例随机传送
     *
     * @param player 玩家实例
     * @param world  世界实例
     * @param biome  群系实例
     */
    public static void randomTeleport(Player player, World world, Biome biome) {
        ConfigurationSection group = getGroupConfigurationSection(player);
        if (group == null) {
            return;
        }

        int min = group.getInt("min");
        int max = group.getInt("max");

        List<String> blackBlock = group.getStringList("blackBlock");
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
            if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR || block.getType() == Material.VOID_AIR) {
                continue;
            }
            if (blackBlock.contains(block.getType().toString())) {
                continue;
            }
            location.setY(location.getY() + 1);

            TeleportUtil.teleport(player, location, new ConcurrentHashMap<>());
            return;
        }

        randomTeleport(player, world, biome);
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
}
