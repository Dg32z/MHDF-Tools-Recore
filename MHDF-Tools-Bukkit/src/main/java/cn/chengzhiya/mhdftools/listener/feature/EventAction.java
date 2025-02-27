package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class EventAction extends AbstractListener {
    public EventAction() {
        super(
                "eventActionSettings.enable"
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        runAction(event.getPlayer(), "玩家加入服务器");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        runAction(event.getPlayer(), "玩家退出服务器");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        runAction(event.getPlayer(), "玩家切换世界");
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        runAction(event.getPlayer(), "玩家聊天");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        runAction(event.getPlayer(), "玩家死亡");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        runAction(event.getPlayer(), "玩家复活");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        runAction(event.getPlayer(), "玩家移动");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        runAction(event.getPlayer(), "玩家传送");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        runAction(event.getPlayer(), "玩家破坏方块");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        runAction(event.getPlayer(), "玩家放置方块");
    }

    /**
     * 获取指定事件的操作列表
     *
     * @param event 事件
     * @return 操作列表
     */
    private List<String> getActionList(Player player, String event) {
        ConfigurationSection actionList = ConfigUtil.getConfig().getConfigurationSection("eventActionSettings.actionList");
        if (actionList == null) {
            return new ArrayList<>();
        }

        List<String> list = new ArrayList<>();
        for (String key : actionList.getKeys(false)) {
            ConfigurationSection action = actionList.getConfigurationSection(key);
            if (action == null) {
                continue;
            }

            String type = action.getString("event");
            if (type == null) {
                continue;
            }

            List<String> world = action.getStringList("world");
            if (!world.isEmpty()) {
                if (!world.contains(player.getWorld().getName())) {
                    continue;
                }
            }

            LogUtil.debug("事件操作类型比对 | 事件名称: {} | 事件类型: {} | 目标类型: {}",
                    key,
                    type,
                    event
            );

            if (Objects.equals(type, event)) {
                list.addAll(action.getStringList("action"));
            }
        }

        LogUtil.debug("对应事件类型操作列表获取成功 | 事件类型: {} | 操作列表: {}",
                event,
                String.valueOf(list)
        );
        return list;
    }

    /**
     * 执行操作
     *
     * @param player 玩家实例
     * @param event  事件
     */
    private void runAction(Player player, String event) {
        ActionUtil.runActionList(player, getActionList(player, event));
    }
}
