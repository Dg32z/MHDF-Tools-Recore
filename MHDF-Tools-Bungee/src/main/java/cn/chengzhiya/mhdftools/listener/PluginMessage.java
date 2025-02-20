package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.Main;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

public final class PluginMessage implements Listener {
    public ServerInfo getServerInfo(PluginMessageEvent event) {
        InetAddress senderAddress = event.getSender().getAddress().getAddress();
        int senderPort = event.getSender().getAddress().getPort();

        for (ServerInfo server : Main.instance.getProxy().getServers().values()) {
            InetAddress serverAddress = server.getAddress().getAddress();

            int serverPort = server.getAddress().getPort();

            if (serverAddress.equals(senderAddress) && serverPort == senderPort) {
                return server;
            }
        }
        return null;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().contains("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        try {
            String subchannel = in.readUTF();
            if (!subchannel.equals("mhdf_tools")) {
                return;
            }

            ServerInfo senderServer = getServerInfo(event);
            if (senderServer == null) {
                return;
            }

            JSONObject data = JSONObject.parseObject(in.readUTF());
            data.put("from", senderServer.getName());

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("mhdf_tools");
            out.writeUTF(data.toJSONString());

            String to = data.getString("to");
            switch (to) {
                case "all" -> {
                    for (ServerInfo server : Main.instance.getProxy().getServers().values()) {
                        sendData(server, out);
                    }
                    return;
                }
                case "me" -> sendData(senderServer, out);
                default -> {
                    ServerInfo server = Main.instance.getProxy().getServerInfo(to);
                    sendData(server, out);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendData(ServerInfo server, ByteArrayDataOutput out) {
        if (server == null) {
            return;
        }

        if (server.getPlayers().isEmpty()) {
            return;
        }

        server.sendData("BungeeCord", out.toByteArray());
    }
}
