package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Metrics;
import cn.chengzhiya.mhdftools.interfaces.Init;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;

public final class BStatsManager implements Init {
    /**
     * 初始化bstats
     */
    @Override
    public void init() {
        if (ConfigUtil.getConfig().getBoolean("bStats")) {
            new Metrics(24887);
        }
    }
}
