package cn.ChengZhiYa.MHDFTools.util.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class AsyncScheduler {
    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.AsyncScheduler asyncScheduler;

    AsyncScheduler() {
        if (MHDFScheduler.isFolia()) {
            asyncScheduler = Bukkit.getAsyncScheduler();
        } else {
            bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public MHDFTask runNow(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskAsynchronously(plugin, () -> task.accept(null)));
        }

        return new MHDFTask(asyncScheduler.runNow(plugin, (o) -> task.accept(null)));
    }

    public MHDFTask runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskLaterAsynchronously(plugin, () -> task.accept(null), delay));
        }

        return new MHDFTask(asyncScheduler.runDelayed(plugin, (o) -> task.accept(null), delay * 50, TimeUnit.MILLISECONDS));
    }

    public MHDFTask runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, long period) {
        if (period < 1) period = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskTimerAsynchronously(plugin, () -> task.accept(null), delay, period));
        }

        return new MHDFTask(asyncScheduler.runAtFixedRate(plugin, (o) -> task.accept(null), delay * 50, period * 50, TimeUnit.MILLISECONDS));
    }

    public void cancel(@NotNull Plugin plugin) {
        if (!MHDFScheduler.isFolia()) {
            bukkitScheduler.cancelTasks(plugin);
            return;
        }

        asyncScheduler.cancelTasks(plugin);
    }
}