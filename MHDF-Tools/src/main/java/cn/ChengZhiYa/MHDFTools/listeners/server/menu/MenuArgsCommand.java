package cn.ChengZhiYa.MHDFTools.listeners.server.menu;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.openMenu;

public final class MenuArgsCommand implements Listener {

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String mapKey = player.getName() + "_ArgsRunCommand";

        if (MapUtil.getStringHashMap().containsKey(mapKey)) {
            String commandInfo = MapUtil.getStringHashMap().get(mapKey);
            String[] commandParts = commandInfo.split("\\|");
            String[] args = event.getMessage().split("\\|");

            for (int i = 0; i < args.length; i++) {
                commandParts[3] = commandParts[3].replaceAll("%" + i, args[i]);
            }

            if (commandParts[0].equals("player")) {
                Bukkit.getScheduler().runTask(MHDFTools.instance, () -> player.chat("/" + PlaceholderAPI.setPlaceholders(player, commandParts[3])));
            } else {
                Bukkit.getScheduler().runTask(MHDFTools.instance, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, commandParts[3])));
            }

            if ("true".equals(commandParts[2])) {
                openMenu(player, commandParts[1]);
            }

            MapUtil.getStringHashMap().remove(mapKey);
        }
    }
}