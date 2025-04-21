package cn.chengzhiya.mhdftools.task;

import cn.chengzhiya.mhdfscheduler.runnable.MHDFRunnable;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class AbstractTask extends MHDFRunnable {
    private final boolean enable;
    private final Long time;

    public AbstractTask(String enableKey, @NotNull Long time) {
        if (enableKey != null && !enableKey.isEmpty()) {
            this.enable = ConfigUtil.getConfig().getBoolean(enableKey);
        } else {
            this.enable = true;
        }
        this.time = time;
    }
}
