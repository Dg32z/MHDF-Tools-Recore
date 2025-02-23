package cn.chengzhiya.mhdftools.util.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class EntityScheduler {
    private BukkitScheduler bukkitScheduler;

    public EntityScheduler() {
        if (!MHDFScheduler.isFolia()) {
            bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public void execute(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable run, @Nullable Runnable retired, long delay) {
        if (!MHDFScheduler.isFolia()) {
            bukkitScheduler.runTaskLater(plugin, run, delay);
            return;
        }

        entity.getScheduler().execute(plugin, run, retired, delay);
    }

    public MHDFTask run(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Consumer<Object> task, @Nullable Runnable retired) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTask(plugin, () -> task.accept(null)));
        }

        return new MHDFTask(Objects.requireNonNull(entity.getScheduler().run(plugin, (o) -> task.accept(null), retired)));
    }

    public MHDFTask runDelayed(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Consumer<Object> task, @Nullable Runnable retired, long delayTicks) {
        if (delayTicks < 1) delayTicks = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskLater(plugin, () -> task.accept(null), delayTicks));
        }
        return new MHDFTask(Objects.requireNonNull(entity.getScheduler().runDelayed(plugin, (o) -> task.accept(null), retired, delayTicks)));
    }

    public MHDFTask runAtFixedRate(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Consumer<Object> task, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1) initialDelayTicks = 1;
        if (periodTicks < 1) periodTicks = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        return new MHDFTask(Objects.requireNonNull(entity.getScheduler().runAtFixedRate(plugin, (o) -> task.accept(null), retired, initialDelayTicks, periodTicks)));
    }
}