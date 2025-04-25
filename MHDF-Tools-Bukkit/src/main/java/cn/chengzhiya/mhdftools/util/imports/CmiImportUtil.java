package cn.chengzhiya.mhdftools.util.imports;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.BungeeCordLocation;
import cn.chengzhiya.mhdftools.entity.database.HomeData;
import cn.chengzhiya.mhdftools.entity.database.WarpData;
import cn.chengzhiya.mhdftools.entity.database.cmi.CmiUserData;
import cn.chengzhiya.mhdftools.entity.database.huskhomes.HuskHomesHomeData;
import cn.chengzhiya.mhdftools.entity.database.huskhomes.HuskHomesPositionData;
import cn.chengzhiya.mhdftools.entity.database.huskhomes.HuskHomesPositionInfoData;
import cn.chengzhiya.mhdftools.entity.database.huskhomes.HuskHomesWarpData;
import cn.chengzhiya.mhdftools.manager.database.CmiDatabaseManager;
import cn.chengzhiya.mhdftools.manager.database.HuskHomesDatabaseManager;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.HomeDataUtil;
import org.bukkit.command.CommandSender;

public final class CmiImportUtil {
    /**
     * 导入Cmi的数据
     *
     * @param sender 命令执行者
     */
    public static void importCmiData(CommandSender sender) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, (task) -> {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.message.start")
                    .replace("{plugin}", "Cmi")
            );
            CmiDatabaseManager cmiDatabaseManager = new CmiDatabaseManager();
            cmiDatabaseManager.connect();
            cmiDatabaseManager.initDao();

            // 导入家数据
            {
                if (ConfigUtil.getConfig().getBoolean("homeSettings.enable")) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "家系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (CmiUserData cmiUserData : cmiDatabaseManager.getCmiUserDataList()) {
                        String[] homeList = cmiUserData.getHomes()
                                .replaceAll("\\$-0%%", ":")
                                .split(";");
                        for (String home : homeList) {
                            String[] data = home.split(":");

                            HomeData homeData = new HomeData();
                            homeData.setHome(data[0]);
                            homeData.setPlayer(cmiUserData.getPlayerUuid());

                            BungeeCordLocation bungeeCordLocation = new BungeeCordLocation(
                                    data[1],
                                    Double.parseDouble(data[2]),
                                    Double.parseDouble(data[3]),
                                    Double.parseDouble(data[4]),
                                    Float.parseFloat(data[6]),
                                    Float.parseFloat(data[5])
                            );

                            homeData.setLocation(bungeeCordLocation);

                            HomeDataUtil.updateHomeData(homeData);
                        }
                    }

                    Long endTime = System.currentTimeMillis();
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "家系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            cmiDatabaseManager.close();
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.message.done")
                    .replace("{plugin}", "Cmi")
            );
        });
    }
}
