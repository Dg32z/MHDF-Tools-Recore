package cn.chengzhiya.mhdftools.util.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class RegionScheduler {
    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.RegionScheduler regionScheduler;

    public RegionScheduler() {
        if (MHDFScheduler.isFolia()) {
            regionScheduler = Bukkit.getRegionScheduler();
        } else {
            bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public void execute(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Runnable run) {
        if (!MHDFScheduler.isFolia()) {
            bukkitScheduler.runTask(plugin, run);
            return;
        }

        regionScheduler.execute(plugin, world, chunkX, chunkZ, run);
    }

    public void execute(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable run) {
        if (!MHDFScheduler.isFolia()) {
            Bukkit.getScheduler().runTask(plugin, run);
            return;
        }

        regionScheduler.execute(plugin, location, run);
    }

    public MHDFTask run(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Consumer<Object> task) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(Bukkit.getScheduler().runTask(plugin, () -> task.accept(null)));
        }

        return new MHDFTask(regionScheduler.run(plugin, world, chunkX, chunkZ, (o) -> task.accept(null)));
    }

    public MHDFTask run(@NotNull Plugin plugin, @NotNull Location location, @NotNull Consumer<Object> task) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(Bukkit.getScheduler().runTask(plugin, () -> task.accept(null)));
        }

        return new MHDFTask(regionScheduler.run(plugin, location, (o) -> task.accept(null)));
    }

    public MHDFTask runDelayed(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Consumer<Object> task, long delayTicks) {
        if (delayTicks < 1) delayTicks = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(Bukkit.getScheduler().runTaskLater(plugin, () -> task.accept(null), delayTicks));
        }

        return new MHDFTask(regionScheduler.runDelayed(plugin, world, chunkX, chunkZ, (o) -> task.accept(null), delayTicks));
    }

    public MHDFTask runDelayed(@NotNull Plugin plugin, @NotNull Location location, @NotNull Consumer<Object> task, long delayTicks) {
        if (delayTicks < 1) delayTicks = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(Bukkit.getScheduler().runTaskLater(plugin, () -> task.accept(null), delayTicks));
        }

        return new MHDFTask(regionScheduler.runDelayed(plugin, location, (o) -> task.accept(null), delayTicks));
    }

    public MHDFTask runAtFixedRate(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1) initialDelayTicks = 1;
        if (periodTicks < 1) periodTicks = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(Bukkit.getScheduler().runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        return new MHDFTask(regionScheduler.runAtFixedRate(plugin, world, chunkX, chunkZ, (o) -> task.accept(null), initialDelayTicks, periodTicks));
    }

    public MHDFTask runAtFixedRate(@NotNull Plugin plugin, @NotNull Location location, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1) initialDelayTicks = 1;
        if (periodTicks < 1) periodTicks = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(Bukkit.getScheduler().runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        return new MHDFTask(regionScheduler.runAtFixedRate(plugin, location, (o) -> task.accept(null), initialDelayTicks, periodTicks));
    }
}