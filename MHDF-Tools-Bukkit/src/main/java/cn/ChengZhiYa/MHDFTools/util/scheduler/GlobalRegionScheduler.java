package cn.ChengZhiYa.MHDFTools.util.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class GlobalRegionScheduler {
    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler globalRegionScheduler;

    GlobalRegionScheduler() {
        if (MHDFScheduler.isFolia()) {
            globalRegionScheduler = Bukkit.getGlobalRegionScheduler();
        } else {
            bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public void execute(@NotNull Plugin plugin, @NotNull Runnable run) {
        if (!MHDFScheduler.isFolia()) {
            bukkitScheduler.runTask(plugin, run);
            return;
        }

        globalRegionScheduler.execute(plugin, run);
    }

    public MHDFTask run(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTask(plugin, () -> task.accept(null)));
        }

        return new MHDFTask(globalRegionScheduler.run(plugin, (o) -> task.accept(null)));
    }

    public MHDFTask runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay) {
        if (delay < 1) delay = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskLater(plugin, () -> task.accept(null), delay));
        }

        return new MHDFTask(globalRegionScheduler.runDelayed(plugin, (o) -> task.accept(null), delay));
    }

    public MHDFTask runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1) initialDelayTicks = 1;
        if (periodTicks < 1) periodTicks = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        return new MHDFTask(globalRegionScheduler.runAtFixedRate(plugin, (o) -> task.accept(null), initialDelayTicks, periodTicks));
    }

    public void cancel(@NotNull Plugin plugin) {
        if (!MHDFScheduler.isFolia()) {
            Bukkit.getScheduler().cancelTasks(plugin);
            return;
        }

        globalRegionScheduler.cancelTasks(plugin);
    }
}