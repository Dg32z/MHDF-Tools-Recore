package cn.ChengZhiYa.MHDFTools.util.runnable;

import cn.ChengZhiYa.MHDFTools.util.scheduler.MHDFScheduler;
import cn.ChengZhiYa.MHDFTools.util.scheduler.MHDFTask;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@SuppressWarnings("unused")
public abstract class MHDFRunnable implements Runnable {
    public MHDFTask mhdfTask;

    public void runTask(JavaPlugin plugin) {
        this.mhdfTask = MHDFScheduler.getGlobalRegionScheduler().run(plugin, (task) -> run());
    }

    public void runTaskLater(JavaPlugin plugin, long delay) {
        this.mhdfTask = MHDFScheduler.getGlobalRegionScheduler().runDelayed(plugin, (task) -> run(), delay);
    }

    public void runTaskTimer(JavaPlugin plugin, long delay, long period) {
        this.mhdfTask = MHDFScheduler.getGlobalRegionScheduler().runAtFixedRate(plugin, (task) -> run(), delay, period);
    }

    public void runTaskAsynchronously(JavaPlugin plugin) {
        this.mhdfTask = MHDFScheduler.getAsyncScheduler().runNow(plugin, (task) -> run());
    }

    public void runTaskLaterAsynchronously(JavaPlugin plugin, long delay) {
        this.mhdfTask = MHDFScheduler.getAsyncScheduler().runDelayed(plugin, (task) -> run(), delay);
    }

    public void runTaskTimerAsynchronously(JavaPlugin plugin, long delay, long period) {
        this.mhdfTask = MHDFScheduler.getAsyncScheduler().runAtFixedRate(plugin, (task) -> run(), delay, period);
    }

    public Plugin getOwner() {
        return mhdfTask.getOwner();
    }

    public boolean isCancelled() {
        return mhdfTask.isCancelled();
    }

    public void cancel() {
        mhdfTask.cancel();
    }
}
