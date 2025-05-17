package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class AbstractListener implements Listener {
    private final boolean enable;

    public AbstractListener() {
        this.enable = true;
    }

    public AbstractListener(@NotNull String enableKey) {
        this.enable = ConfigUtil.getConfig().getBoolean(enableKey);
    }

    public AbstractListener(List<String> enableKeyList) {
        boolean enable = true;
        for (String enableKey : enableKeyList) {
            if (enableKey == null || enableKey.isEmpty()) {
                continue;
            }

            enable = ConfigUtil.getConfig().getBoolean(enableKey);
        }

        this.enable = enable;
    }
}
