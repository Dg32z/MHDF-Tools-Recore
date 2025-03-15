package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.enums.MessageType;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.feature.ChatUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

public final class Chat extends AbstractListener {
    public Chat() {
        super(
                "chatSettings.enable"
        );
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (!player.hasPermission("mhdftools.bypass.color")) {
            message = ChatColor.stripColor(ColorUtil.color(message));
        }

        if (!player.hasPermission("mhdftools.bypass.minimessage")) {
            Pattern pattern = Pattern.compile("</?[a-zA-Z0-9_:-]+>");
            message = pattern.matcher(message).replaceAll("");
        }

        message = ChatUtil.applyBlackWord(player, message);
        message = ChatUtil.applyShowItem(player, message);

        event.setCancelled(true);
        Main.instance.getBungeeCordManager().broadcastMessage(
                MessageType.MINI_MESSAGE,
                ColorUtil.legacyToMiniMessage(ChatUtil.getFormatMessage(player, message))
        );
    }
}
