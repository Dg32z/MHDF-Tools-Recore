package cn.chengzhiya.mhdftools.listener.redisMessage;

import cn.chengzhiya.mhdftools.listener.AbstractRedisMessageListener;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class SendMesasge extends AbstractRedisMessageListener {
    public SendMesasge() {
        super(
                null,
                "sendMessage"
        );
    }

    @Override
    public void onMessage(String message) {
        JSONObject data = JSONObject.parseObject(message);

        String playerName = data.getString("playerName");
        String text = data.getString("message");
        LogUtil.debug("发送跨服消息 | 目标玩家: {} | 消息: {}",
                playerName,
                text
        );

        if (playerName.equals("all")) {
            ActionUtil.broadcastMessage(text);
            return;
        }

        if (playerName.equals("console")) {
            LogUtil.log(text);
            return;
        }

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return;
        }

        ActionUtil.sendMessage(player, text);
    }
}
