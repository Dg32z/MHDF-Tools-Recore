package cn.chengzhiya.mhdftools.util;

import cn.chengzhiya.mhdftools.Main;

public final class PluginUtil {
    /**
     * 获取插件版本号
     *
     * @return 插件版本号
     */
    public static String getVersion() {
        return Main.instance.getDescription().getVersion();
    }

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    public static String getName() {
        return Main.instance.getDescription().getName();
    }
}
