package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.ChatIgnoreDataUtil;
import cn.chengzhiya.mhdftools.util.feature.AtUtil;
import cn.chengzhiya.mhdftools.util.feature.ChatUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;
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

        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings");
        if (config == null) {
            return;
        }

        // 聊天延迟
        if (config.getBoolean("delay.enable")) {
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
        if (!player.hasPermission("mhdftools.bypass.chat.color")) {
            message = ChatColor.stripColor(ColorUtil.legacyColor(message));
        }

        // 限制使用miniMessage
        if (!player.hasPermission("mhdftools.bypass.chat.minimessage")) {
            Pattern pattern = Pattern.compile("</?[a-zA-Z0-9_:-]+>");
            message = pattern.matcher(message).replaceAll("");
        }

        // 刷屏限制
        if (config.getBoolean("spam.enable")) {
            if (!player.hasPermission("mhdftools.bypass.chat.spam")) {
                String spamData = Main.instance.getCacheManager().get(player.getName() + "_spam");
                if (spamData != null && spamData.equals(message)) {
                    ActionUtil.sendMessage(player, LangUtil.i18n("chat.spam"));
                    event.setCancelled(true);
                    return;
                }
            }
        }

        int delay = config.getInt("delay.delay");
        Main.instance.getCacheManager().put(player.getName() + "_delay", String.valueOf(delay));
        Main.instance.getCacheManager().put(player.getName() + "_spam", message);

        // 替换词
        if (!player.hasPermission("mhdftools.bypass.chat.replaceWord")) {
            message = ChatUtil.applyBlackWord(message);
        }

        // 展示物品
        message = ChatUtil.applyShowItem(player, message);

        // AT玩家
        Set<String> atList = AtUtil.getAtList(player, message);
        message = ChatUtil.applyAt(message, atList);
        Main.instance.getBungeeCordManager().atList(atList);

        // 发送消息
        Main.instance.getBungeeCordManager().sendMessage(
                "console",
                ChatUtil.getFormatMessage(player, message)
        );
        for (String target : Main.instance.getBungeeCordManager().getPlayerList()) {
            if (config.getBoolean("ignore.enable")) {
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

        event.setCancelled(true);
    }
}
