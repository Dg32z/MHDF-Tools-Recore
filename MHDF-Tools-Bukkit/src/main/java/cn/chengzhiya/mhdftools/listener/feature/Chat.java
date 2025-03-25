package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.ChatIgnoreDataUtil;
import cn.chengzhiya.mhdftools.util.feature.ChatUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

        // 聊天延迟
        if (ConfigUtil.getConfig().getBoolean("chatSettings.delay.enable")) {
            if (!player.hasPermission("mhdftools.bypass.chat.delay")) {
                String delayData = Main.instance.getCacheManager().get(player.getName() + "_delay");
                if (delayData != null) {
                    ActionUtil.sendMessage(player, LangUtil.i18n("chat.delay")
                            .replace("{delay}", delayData)
                    );
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // 限制使用颜色符号
        if (!player.hasPermission("mhdftools.bypass.color")) {
            message = ChatColor.stripColor(ColorUtil.legacyColor(message));
        }

        // 限制使用miniMessage
        if (!player.hasPermission("mhdftools.bypass.minimessage")) {
            Pattern pattern = Pattern.compile("</?[a-zA-Z0-9_:-]+>");
            message = pattern.matcher(message).replaceAll("");
        }

        // 刷屏限制
        if (ConfigUtil.getConfig().getBoolean("chatSettings.spam.enable")) {
            if (!player.hasPermission("mhdftools.bypass.chat.spam")) {
                String spamData = Main.instance.getCacheManager().get(player.getName() + "_spam");
                if (spamData != null && spamData.equals(message)) {
                    ActionUtil.sendMessage(player, LangUtil.i18n("chat.spam"));
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

        Main.instance.getBungeeCordManager().sendMessage(
                "console",
                ChatUtil.getFormatMessage(player, message)
        );
        for (String target : Main.instance.getBungeeCordManager().getPlayerList()) {
            if (ConfigUtil.getConfig().getBoolean("chatSettings.ignore.enable")) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
                if (ChatIgnoreDataUtil.isChatIgnore(targetPlayer, player)) {
                    continue;
                }
            }

            Main.instance.getBungeeCordManager().sendMessage(
                    target,
                    ChatUtil.getFormatMessage(player, message)
            );
        }
    }
}
