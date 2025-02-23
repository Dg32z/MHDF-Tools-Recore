package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.Metrics;
import cn.ChengZhiYa.MHDFTools.interfaces.Init;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;

public final class BStatsManager implements Init {
    /**
     * 启动bstats
     */
    @Override
    public void init() {
        if (ConfigUtil.getConfig().getBoolean("bStats")) {
            new Metrics(17154);
        }
    }
}
