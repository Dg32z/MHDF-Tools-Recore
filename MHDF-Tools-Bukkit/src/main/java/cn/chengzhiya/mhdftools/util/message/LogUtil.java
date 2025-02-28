package cn.chengzhiya.mhdftools.util.message;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import org.bukkit.Bukkit;

public final class LogUtil {
    private static final String CONSOLE_PREFIX = "[MHDF-Tools] ";
    private static final String DEBUG_PREFIX = "[MHDF-Tools-Debug] ";

    /**
     * 日志消息
     *
     * @param message 内容
     * @param args    参数
     */
    public static void log(String message, String... args) {
        for (Object var : args) {
            message = message.replaceFirst("\\{}", var.toString());
        }
        Bukkit.getConsoleSender().sendMessage(ColorUtil.color(CONSOLE_PREFIX + message));
    }

    /**
     * 调试消息
     *
     * @param message 内容
     * @param args    参数
     */
    public static void debug(String message, String... args) {
        if (!ConfigUtil.getConfig().getBoolean("debug")) {
            return;
        }

        log(DEBUG_PREFIX + message, args);
    }
}
