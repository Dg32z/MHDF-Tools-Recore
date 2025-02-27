package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.BungeeCordLocation;
import cn.chengzhiya.mhdftools.enums.MessageType;
import cn.chengzhiya.mhdftools.interfaces.Init;
import cn.chengzhiya.mhdftools.listener.PluginMessage;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

@Getter
public final class BungeeCordManager implements Init {
    private final PluginMessageListener messageListener = new PluginMessage();
    private final List<String> bungeeCordPlayerList = new ArrayList<>();
    @Setter
    private String serverName = "无";

    /**
     * 初始化群组模式
     */
    @Override
    public void init() {
        if (isBungeeCordMode()) {
            getServer().getMessenger().registerOutgoingPluginChannel(Main.instance, "BungeeCord");
            getServer().getMessenger().registerIncomingPluginChannel(Main.instance, "BungeeCord", getMessageListener());
        }
    }

    /**
     * 关闭群组模式
     */
    public void close() {
        if (isBungeeCordMode()) {
            getServer().getMessenger().unregisterOutgoingPluginChannel(Main.instance, "BungeeCord");
            getServer().getMessenger().unregisterIncomingPluginChannel(Main.instance, "BungeeCord", getMessageListener());
        }
    }

    /**
     * 检测是否开启群组模式
     *
     * @return 结果
     */
    public boolean isBungeeCordMode() {
        return ConfigUtil.getConfig().getBoolean("bungeeCordSettings.enable");
    }

    /**
     * 给BC插件通道发送指定消息数据实例
     *
     * @param out 消息数据实例
     */
    public void sendPluginMessage(ByteArrayDataOutput out) {
        if (!isBungeeCordMode()) {
            return;
        }

        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (playerList.isEmpty()) {
            return;
        }

        playerList.get(0).sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
    }

    /**
     * 发送梦之工具插件消息
     *
     * @param data 消息数据实例
     */
    public void sendMhdfToolsPluginMessage(JSONObject data) {
        if (data.getJSONObject("params") == null) {
            data.put("params", new JSONObject());
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("mhdf_tools");
        out.writeUTF(data.toJSONString());

        sendPluginMessage(out);
    }

    /**
     * 传送指定玩家ID到指定玩家ID的服务器
     *
     * @param playerName 被传送的玩家ID
     * @param targetName 传送到的玩家ID
     */
    public void teleportPlayerServer(String playerName, String targetName) {
        Player player = Bukkit.getPlayer(playerName);
        Player target = Bukkit.getPlayer(targetName);
        if (player != null && target != null) {
            player.teleport(target);
            return;
        }

        JSONObject data = new JSONObject();
        data.put("action", "teleportPlayer");
        data.put("to", "all");

        JSONObject params = new JSONObject();
        params.put("playerName", playerName);
        params.put("targetName", targetName);

        data.put("params", params);

        sendMhdfToolsPluginMessage(data);
    }

    /**
     * 将指定玩家ID传送至指定群组位置实例
     *
     * @param playerName         玩家ID
     * @param bungeeCordLocation 群组位置实例
     */
    public void teleportLocation(String playerName, BungeeCordLocation bungeeCordLocation) {
        if (bungeeCordLocation.getServer().equals(getServerName())) {
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                return;
            }

            player.teleport(bungeeCordLocation.toLocation());
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(playerName);
        out.writeUTF(bungeeCordLocation.getServer());

        Main.instance.getCacheManager().put(playerName + "_tpLocation", bungeeCordLocation.toBase64());

        sendPluginMessage(out);
    }

    /**
     * 向指定玩家ID发送指定消息文本
     *
     * @param playerName 玩家ID
     * @param message    消息文本
     */
    public void sendMessage(String playerName, MessageType type, String message) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            switch (type) {
                case MINI_MESSAGE -> Main.adventure.player(player).sendMessage(ColorUtil.miniMessage(message));
                case LEGACY -> player.sendMessage(ColorUtil.color(message));
            }
            return;
        }

        JSONObject data = new JSONObject();
        data.put("action", "sendMessage");
        data.put("to", "all");

        JSONObject params = new JSONObject();
        params.put("playerName", playerName);
        params.put("type", type.name());
        params.put("message", message);

        data.put("params", params);

        sendMhdfToolsPluginMessage(data);
    }

    /**
     * 修改指定玩家ID的游戏模式为游戏模式实例
     *
     * @param playerName 玩家ID
     * @param gameMode   游戏模式实例
     */
    public void setGameMode(String playerName, GameMode gameMode) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.setGameMode(gameMode);
            return;
        }

        JSONObject data = new JSONObject();
        data.put("action", "setGameMode");
        data.put("to", "all");

        JSONObject params = new JSONObject();
        params.put("playerName", playerName);
        params.put("gameMode", gameMode.name());

        data.put("params", params);

        sendMhdfToolsPluginMessage(data);
    }

    /**
     * 更新BC玩家列表数据
     */
    public void updateBungeeCordPlayerList() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");

        sendPluginMessage(out);
    }

    /**
     * 获取在线玩家列表
     *
     * @return 在线玩家列表
     */
    public List<String> getPlayerList() {
        if (isBungeeCordMode()) {
            return getBungeeCordPlayerList();
        }

        return getBukkitPlayerList();
    }

    /**
     * 获取子服在线玩家列表
     *
     * @return 子服在线玩家列表
     */
    public List<String> getBukkitPlayerList() {
        List<String> list = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            list.add(name);
        }
        return list;
    }

    /**
     * 更新BC服务器名称数据
     */
    public void updateServerName() {
        JSONObject data = new JSONObject();
        data.put("action", "serverInfo");
        data.put("to", "me");

        sendMhdfToolsPluginMessage(data);
    }

    /**
     * 判断指定玩家ID的玩家是否在线
     *
     * @param name 玩家ID
     * @return 结果
     */
    public boolean ifPlayerOnline(String name) {
        return getPlayerList().contains(name);
    }
}
