package cn.ChengZhiYa.MHDFTools.util;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

@SuppressWarnings("unused")
public final class BossBarUtil {
    /**
     * 构建boss血条实例
     *
     * @param title 标题
     * @param color 颜色
     * @param style 样式
     * @return boss血条实例
     */
    public static BossBar getBossBar(String title, BossBar.Color color, BossBar.Overlay style) {
        return BossBar.bossBar(Component.text(title), 1f, color, style);
    }

    /**
     * 构建boss血条实例
     *
     * @param title 标题
     * @param color 颜色
     * @return boss血条实例
     */
    public static BossBar getBossBar(String title, BossBar.Color color) {
        return getBossBar(title, color, BossBar.Overlay.PROGRESS);
    }

    /**
     * 构建boss血条实例
     *
     * @param title 标题
     * @return boss血条实例
     */
    public static BossBar getBossBar(String title) {
        return getBossBar(title, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);
    }
}
