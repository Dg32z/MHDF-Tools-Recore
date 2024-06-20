package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCord.*;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class PlayerTpaTask extends BukkitRunnable {
    @Override
    public void run() {
        if (!MHDFTools.instance.getConfig().getBoolean("TpaSettings.Enable")) {
            return;
        }

        MapUtil.getStringHasMap().keySet().forEach(key -> {
            if (key.toString().contains("_TPAPlayerName")) {
                String playerName = key.toString().replaceAll("_TPAPlayerName", "");
                String targetPlayerName = MapUtil.getStringHasMap().get(key);
                int time = MapUtil.getIntHasMap().get(playerName + "_TPATime");

                if (ifPlayerOnline(playerName)) {
                    if (ifPlayerOnline(targetPlayerName)) {
                        if (time >= 0) {
                            MapUtil.getIntHasMap().put(playerName + "_TPATime", time - 1);
                        } else {
                            SendMessage(playerName, i18n("Tpa.TimeOutDone", targetPlayerName));
                            SendMessage(targetPlayerName, i18n("Tpa.TimeOut", playerName));
                            CancelTpa(playerName);
                        }
                    } else {
                        SendMessage(playerName, i18n("Tpa.Offline", targetPlayerName));
                        CancelTpa(playerName);
                    }
                } else {
                    CancelTpa(playerName);
                }
            }
        });
    }
}