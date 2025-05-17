package cn.chengzhiya.mhdftools.task;

import cn.chengzhiya.mhdfscheduler.runnable.MHDFRunnable;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class AbstractTask extends MHDFRunnable {
    private final boolean enable;
    private final Long time;

    public AbstractTask(@NotNull Long time) {
        this.enable = true;
        this.time = time;
    }

    public AbstractTask(@NotNull String enableKey, @NotNull Long time) {
        this.enable = ConfigUtil.getConfig().getBoolean(enableKey);
        this.time = time;
    }

    public AbstractTask(List<String> enableKeyList, @NotNull Long time) {
        boolean enable = true;
        for (String enableKey : enableKeyList) {
            if (enableKey == null || enableKey.isEmpty()) {
                continue;
            }

            enable = ConfigUtil.getConfig().getBoolean(enableKey);
        }

        this.enable = enable;
        this.time = time;
    }
}
