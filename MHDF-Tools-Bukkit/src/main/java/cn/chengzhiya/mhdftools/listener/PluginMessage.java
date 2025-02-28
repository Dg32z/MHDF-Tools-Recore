package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.enums.MessageType;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import com.alibaba.fastjson.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public final class PluginMessage implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player messagePlayer, byte @NotNull [] messageData) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(messageData));

        try {
            String subchannel = in.readUTF();
            switch (subchannel) {
                case "mhdf_tools" -> {
                    JSONObject data = JSONObject.parseObject(in.readUTF());

                    LogUtil.debug("收到来自群组端的梦之工具消息 | 消息: {}",
                            data.toJSONString()
                    );

                    String action = data.getString("action");
                    String from = data.getString("from");
                    String to = data.getString("to");
                    JSONObject params = data.getJSONObject("params");

                    switch (action) {
                        case "serverInfo" -> {
                            LogUtil.debug("更新服务器名称 | 名称: {}",
                                    from
                            );
                            Main.instance.getBungeeCordManager().setServerName(from);
                        }
                        case "sendMessage" -> {
                            String playerName = params.getString("playerName");
                            Player player = Bukkit.getPlayer(playerName);
                            if (player == null) {
                                return;
                            }

                            String type = params.getString("type");
                            MessageType messageType = MessageType.valueOf(type);

                            String message = params.getString("message");
                            LogUtil.debug("发送跨服消息 | 目标玩家: {} | 消息类型: {} | 消息: {}",
                                    playerName,
                                    type,
                                    message
                            );

                            switch (messageType) {
                                case MINI_MESSAGE ->
                                        Main.adventure.player(player).sendMessage(ColorUtil.miniMessage(message));
                                case LEGACY -> player.sendMessage(ColorUtil.color(message));
                            }
                        }
                        case "setGameMode" -> {
                            String playerName = params.getString("playerName");
                            Player player = Bukkit.getPlayer(playerName);
                            if (player == null) {
                                return;
                            }

                            String gamemode = params.getString("gameMode");
                            LogUtil.debug("修改跨服游戏模式 | 目标玩家: {} | 游戏模式: {}",
                                    playerName,
                                    gamemode
                            );
                            player.setGameMode(GameMode.valueOf(gamemode));
                        }
                    }
                }
                case "PlayerList" -> {
                    in.readUTF();
                    String playerList = in.readUTF();

                    LogUtil.debug("更新在线玩家列表 | 在线列表: {}",
                            playerList
                    );

                    Main.instance.getBungeeCordManager().getPlayerList().clear();
                    Main.instance.getBungeeCordManager().getPlayerList().addAll(List.of(playerList.split(", ")));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
