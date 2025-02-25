package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.enums.MessageType;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
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

                    String action = data.getString("action");
                    String form = data.getString("form");
                    String to = data.getString("to");
                    JSONObject params = data.getJSONObject("params");

                    switch (action) {
                        case "serverInfo" -> Main.instance.getBungeeCordManager().setServerName(form);
                        case "sendMessage" -> {
                            String playerName = params.getString("playerName");
                            Player player = Bukkit.getPlayer(playerName);
                            if (player == null) {
                                return;
                            }

                            String type = params.getString("type");
                            MessageType messageType = MessageType.valueOf(type);

                            String message = params.getString("message");
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

                            GameMode gameMode = GameMode.valueOf(params.getString("gameMode"));
                            player.setGameMode(gameMode);
                        }
                    }
                }
                case "PlayerList" -> {
                    in.readUTF();
                    Main.instance.getBungeeCordManager().getPlayerList().clear();
                    Main.instance.getBungeeCordManager().getPlayerList().addAll(List.of(in.readUTF().split(", ")));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
