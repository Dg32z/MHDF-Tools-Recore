package cn.ChengZhiYa.MHDFTools.util;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class BungeeCordUtil {
    @Getter
    private static final List<String> bungeeCordPlayerList = new ArrayList<>();
    @Getter
    @Setter
    private static String serverName = "";

    /**
     * 检测是否开启群组模式
     *
     * @return 结果
     */
    public static boolean isBungeeCordMode() {
        return ConfigUtil.getConfig().getBoolean("bungeeCordSettings.enable");
    }

    /**
     * 给BC插件通道发送指定消息数据实例
     *
     * @param out 消息数据实例
     */
    public static void sendPluginMessage(ByteArrayDataOutput out) {
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (playerList.isEmpty()) {
            return;
        }

        playerList.get(0).sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
    }

    /**
     * 更新BC玩家列表数据
     */
    public static void updateBungeeCordPlayerList() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");

        sendPluginMessage(out);
    }

    /**
     * 发送梦之工具插件消息
     *
     * @param data 消息数据实例
     */
    public static void sendMhdfToolsPluginMessage(JSONObject data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("mhdf_tools");
        out.writeUTF(data.toJSONString());
    }

    /**
     * 更新BC服务器名称数据
     */
    public static void updateServerName() {
        JSONObject data = new JSONObject();
        data.put("action", "serverInfo");
        data.put("to", "me");

        sendMhdfToolsPluginMessage(data);
    }

    /**
     * 获取在线玩家列表
     *
     * @return 在线玩家列表
     */
    public static List<String> getPlayerList() {
        if (isBungeeCordMode()) {
            updateBungeeCordPlayerList();
            return getBungeeCordPlayerList();
        }

        List<String> list = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            list.add(name);
        }
        return list;
    }
}
