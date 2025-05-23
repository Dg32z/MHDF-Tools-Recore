package cn.chengzhiya.mhdftools.listener.redisMessage;

import cn.chengzhiya.mhdftools.listener.AbstractRedisMessageListener;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class SetGameMode extends AbstractRedisMessageListener {
    public SetGameMode() {
        super(
                "gamemodeSettings.enable",
                "setGameMode"
        );
    }

    @Override
    public void onMessage(String message) {
        JSONObject data = JSONObject.parseObject(message);

        String playerName = data.getString("playerName");
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return;
        }

        String gamemode = data.getString("gameMode");
        LogUtil.debug("修改跨服游戏模式 | 目标玩家: {} | 游戏模式: {}",
                playerName,
                gamemode
        );
        player.setGameMode(GameMode.valueOf(gamemode));
    }
}
