package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.task.AbstractTask;

@SuppressWarnings("unused")
public final class BungeeCordDataUpdate extends AbstractTask {
    public BungeeCordDataUpdate() {
        super(
                "bungeeCordSettings.enable",
                20L
        );
    }

    @Override
    public void run() {
        Main.instance.getBungeeCordManager().updateServerName();
        Main.instance.getBungeeCordManager().updateBungeeCordPlayerList();
    }
}
