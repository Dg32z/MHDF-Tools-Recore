package cn.ChengZhiYa.MHDFTools.listener.feature;

import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.util.config.CustomMenuConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.CustomMenuUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public final class CustomMenu extends AbstractListener {
    public CustomMenu() {
        super(
                "customMenuSettings.enable"
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String[] args = event.getMessage().substring(1).split(" ");
        for (String key : CustomMenuConfigUtil.getCustomMenuList()) {
            YamlConfiguration config = CustomMenuConfigUtil.getCustomMenuConfig(key + ".yml");

            if (!config.getStringList("commands").contains(config.getString(args[0]))) {
                continue;
            }

            CustomMenuUtil.openCustomMenu(player, key);
            event.setCancelled(true);
            return;
        }
    }
}
