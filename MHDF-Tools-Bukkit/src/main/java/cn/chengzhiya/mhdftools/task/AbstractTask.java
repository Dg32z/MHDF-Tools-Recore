package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.runnable.MHDFRunnable;
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
