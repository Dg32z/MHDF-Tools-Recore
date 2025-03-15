package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.enums.MessageType;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

public final class ChatFormat extends AbstractListener {
    public ChatFormat() {
        super(
                "chatFormatSettings.enable"
        );
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        String group = getGroup(player);
        String format = ConfigUtil.getConfig().getString("chatFormatSettings." + group + ".format");
        if (format == null) {
            return;
        }

        String formatMessage = Main.instance.getPluginHookManager().getPlaceholderAPIHook()
                .placeholder(player, format)
                .replace("{message}", message);

        event.setCancelled(true);
        Main.instance.getBungeeCordManager().broadcastMessage(MessageType.LEGACY, formatMessage);
    }

    /**
     * 获取指定玩家所在的组
     *
     * @param player 玩家实例
     * @return 组名称
     */
    private String getGroup(Player player) {
        List<String> groupList = player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("mhdftools.group.chatformat."))
                .map(permission -> permission.replace("mhdftools.group.chatformat.", ""))
                .toList();

        int maxWeight = 0;
        String maxWeightGroup = "default";

        for (String group : groupList) {
            int weight = ConfigUtil.getConfig().getInt("chatFormatSettings." + group + ".weight");

            if (weight > maxWeight) {
                maxWeight = weight;
                maxWeightGroup = group;
            }
        }

        return maxWeightGroup;
    }
}
