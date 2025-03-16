package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.MinecraftLangUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;
import java.util.Random;

public final class ChatUtil {
    /**
     * 处理文本中的敏感词
     *
     * @param player  玩家实例
     * @param message 文本
     * @return 处理后的文本
     */
    public static String applyBlackWord(Player player, String message) {
        if (player.hasPermission("mhdftools.bypass.blackword")) {
            return message;
        }

        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings.blackWord");
        if (config == null) {
            return message;
        }

        if (!config.getBoolean("enable")) {
            return message;
        }

        String type = config.getString("replace.type");
        if (type == null) {
            return message;
        }

        for (String s : config.getStringList("word")) {
            if (!message.contains(s)) {
                continue;
            }

            switch (type) {
                case "line" -> {
                    List<String> lineList = ConfigUtil.getConfig().getStringList("blackWordSettings.replace.lineList");
                    if (lineList.isEmpty()) {
                        break;
                    }

                    message = lineList.get(new Random().nextInt(lineList.size()));
                }
                case "word" -> {
                    String word = ConfigUtil.getConfig().getString("blackWordSettings.replace.word");
                    if (word == null) {
                        break;
                    }

                    message = message.replace(s, word);
                }
            }
        }

        return message;
    }

    /**
     * 获取处理展示物品后的文本
     *
     * @param player  玩家实例
     * @param message 文本
     * @return 处理后的文本
     */
    public static String applyShowItem(Player player, String message) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings.showItem");
        if (config == null) {
            return message;
        }

        if (!config.getBoolean("enable")) {
            return message;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        String format = config.getString("format");
        if (format == null) {
            return message;
        }
        format = format
                .replace("{name}", MinecraftLangUtil.getItemName(item))
                .replace("{amount}", String.valueOf(item.getAmount()));

        for (String s : config.getStringList("word")) {
            message = message.replace(s,
                    MiniMessage.miniMessage().serialize(
                            Component.text(format).hoverEvent(item.asHoverEvent())
                    ) + "</hover>"
            );
        }

        return message;
    }

    /**
     * 获取处理格式后的文本
     *
     * @param player  玩家实例
     * @param message 文本
     * @return 处理后的文本
     */
    public static String getFormatMessage(Player player, String message) {
        String group = getGroup(player);

        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings.format");

        String format;
        if (config == null || !config.getBoolean("enable")) {
            format = "<{player}> {message}";
        } else {
            format = config.getString(group + ".format");
        }

        return Main.instance.getPluginHookManager().getPlaceholderAPIHook()
                .placeholder(player, format)
                .replace("{player}", player.getName())
                .replace("{message}", message);
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
                .filter(permission -> permission.startsWith("mhdftools.group.chatformat."))
                .map(permission -> permission.replace("mhdftools.group.chatformat.", ""))
                .toList();

        int maxWeight = 0;
        String maxWeightGroup = "default";

        for (String group : groupList) {
            int weight = ConfigUtil.getConfig().getInt("chatSettings.format." + group + ".weight");

            if (weight > maxWeight) {
                maxWeight = weight;
                maxWeightGroup = group;
            }
        }

        return maxWeightGroup;
    }
}
