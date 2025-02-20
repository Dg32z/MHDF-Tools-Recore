package cn.ChengZhiYa.MHDFTools.listener;

import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import com.alibaba.fastjson.JSONObject;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public final class PluginMessage implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            String subchannel = in.readUTF();
            switch (subchannel) {
                case "mhdf_tools" -> {
                    JSONObject data = JSONObject.parseObject(in.readUTF());
                    String action = data.getString("action");
                    String form = data.getString("form");
                    String to = data.getString("to");

                    switch (action) {
                        case "serverInfo" -> BungeeCordUtil.setServerName(form);
                    }
                }
                case "PlayerList" -> {
                    in.readUTF();
                    BungeeCordUtil.getPlayerList().clear();
                    BungeeCordUtil.getPlayerList().addAll(List.of(in.readUTF().split(", ")));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
