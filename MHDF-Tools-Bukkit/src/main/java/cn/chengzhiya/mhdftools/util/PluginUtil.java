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
}
