package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.entity.data.VanishStatus;
import cn.chengzhiya.mhdftools.task.AbstractTask;
import cn.chengzhiya.mhdftools.util.database.VanishStatusUtil;
import cn.chengzhiya.mhdftools.util.feature.VanishUtil;
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
