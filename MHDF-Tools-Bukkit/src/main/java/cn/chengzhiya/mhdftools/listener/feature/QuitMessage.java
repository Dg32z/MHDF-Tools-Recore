package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

public final class QuitMessage extends AbstractListener {
    public QuitMessage() {
        super(
                "quitMessageSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String message = ConfigUtil.getConfig().getString("quitMessageSettings." + getGroup(player) + ".message");
        if (message == null) {
            event.quitMessage(null);
            return;
        }

        event.quitMessage(ColorUtil.color(Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, message))
                .replace("{player}", player.getName())
        );
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
                .filter(permission -> permission.startsWith("mhdftools.group.quitmessage."))
                .map(permission -> permission.replace("mhdftools.group.quitmessage.", ""))
                .toList();

        int maxWeight = 0;
        String maxWeightGroup = "default";

        for (String group : groupList) {
            int weight = ConfigUtil.getConfig().getInt("quitMessageSettings." + group + ".weight");

            if (weight > maxWeight) {
                maxWeight = weight;
                maxWeightGroup = group;
            }
        }

        return maxWeightGroup;
    }
}
