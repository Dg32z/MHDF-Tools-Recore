package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.interfaces.Init;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import org.bstats.bukkit.Metrics;

public final class BStatsManager implements Init {
    /**
     * 启动bstats
     */
    @Override
    public void init() {
        if (ConfigUtil.getConfig().getBoolean("bStats")) {
            new Metrics(Main.instance, 17154);
        }
    }
}
