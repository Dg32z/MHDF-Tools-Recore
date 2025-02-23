package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.menu.fastuse.CustomMenu;
import org.bukkit.entity.Player;

public final class CustomMenuUtil {
    /**
     * 为指定玩家实例打开自定义菜单
     *
     * @param player 玩家实例
     * @param id     菜单ID
     */
    public static void openCustomMenu(Player player, String id) {
        CustomMenu customMenu = new CustomMenu(player, id);
        customMenu.openMenu();
    }
}
