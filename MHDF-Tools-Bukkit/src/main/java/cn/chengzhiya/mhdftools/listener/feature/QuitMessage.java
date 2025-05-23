package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.GroupUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.feature.NickUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public final class QuitMessage extends AbstractListener {
    public QuitMessage() {
        super(
                "quitMessageSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("quitMessageSettings");
        if (config == null) {
            return;
        }

        if (config.getBoolean("removeMessage")) {
            setQuitMessage(event, null);
            return;
        }

        String message = config.getString(GroupUtil.getGroup(player, config, "mhdftools.group.quitmessage.") + ".message");
        if (message == null) {
            setQuitMessage(event, null);
            return;
        }

        setQuitMessage(event, ColorUtil.color(Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, message))
                .replace("{player}", NickUtil.getName(player))
        );
    }

    /**
     * 设置退服提示
     *
     * @param event   进服事件实例
     * @param message 文本实例
     */
    private void setQuitMessage(PlayerQuitEvent event, TextComponent message) {
        if (Main.instance.isNativeSupportAdventureApi()) {
            event.quitMessage(message);
        } else {
            event.setQuitMessage(message.toLegacyString());
        }
    }
}
