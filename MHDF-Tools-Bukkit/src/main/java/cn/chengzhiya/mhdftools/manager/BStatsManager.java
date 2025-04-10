package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Metrics;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;

public final class BStatsManager {
    /**
     * 初始化bstats
     */
    public void init() {
        if (ConfigUtil.getConfig().getBoolean("bStats")) {
            new Metrics(24887);
        }
    }
}
