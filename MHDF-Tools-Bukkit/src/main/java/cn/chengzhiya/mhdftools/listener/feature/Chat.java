package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
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

        if (ConfigUtil.getConfig().getBoolean("chatSettings.delay.enable")) {
            if (!player.hasPermission("mhdftools.bypass.chat.delay")) {
                String delayData = Main.instance.getCacheManager().get(player.getName() + "_delay");
                if (delayData != null) {
                    player.sendMessage(LangUtil.i18n("chat.delay")
                            .replace("{delay}", delayData)
                    );
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (!player.hasPermission("mhdftools.bypass.color")) {
            message = ChatColor.stripColor(ColorUtil.legacyColor(message));
        }

        if (!player.hasPermission("mhdftools.bypass.minimessage")) {
            Pattern pattern = Pattern.compile("</?[a-zA-Z0-9_:-]+>");
            message = pattern.matcher(message).replaceAll("");
        }

        if (ConfigUtil.getConfig().getBoolean("chatSettings.spam.enable")) {
            if (!player.hasPermission("mhdftools.bypass.chat.spam")) {
                String spamData = Main.instance.getCacheManager().get(player.getName() + "_spam");
                if (spamData != null && spamData.equals(message)) {
                    player.sendMessage(LangUtil.i18n("chat.spam"));
                    event.setCancelled(true);
                    return;
                }
            }
        }

        int delay = ConfigUtil.getConfig().getInt("chatSettings.delay.delay");
        Main.instance.getCacheManager().put(player.getName() + "_delay", String.valueOf(delay));
        Main.instance.getCacheManager().put(player.getName() + "_spam", message);

        message = ChatUtil.applyBlackWord(player, message);
        message = ChatUtil.applyShowItem(player, message);

        event.setCancelled(true);
        Main.instance.getBungeeCordManager().broadcastMessage(
                ChatUtil.getFormatMessage(player, message)
        );
    }
}
