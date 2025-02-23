package cn.ChengZhiYa.MHDFTools.task.feature;

import cn.ChengZhiYa.MHDFTools.entity.data.VanishStatus;
import cn.ChengZhiYa.MHDFTools.task.AbstractTask;
import cn.ChengZhiYa.MHDFTools.util.database.VanishStatusUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public final class AutoChangeVanish extends AbstractTask {
    public AutoChangeVanish() {
        super(
                "vanishSettings.enable",
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            VanishStatus vanishStatus = VanishStatusUtil.getVanishStatus(player);

            if (!vanishStatus.isEnable()) {
                continue;
            }

            VanishUtil.enableVanish(player);
        }
    }
}
