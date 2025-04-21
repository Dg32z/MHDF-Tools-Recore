package cn.chengzhiya.mhdftools.placeholder;

import cn.chengzhiya.mhdftools.interfaces.Placeholder;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class AbstractPlaceholder implements Placeholder {
    private final boolean enable;

    public AbstractPlaceholder(String enableKey) {
        if (enableKey != null && !enableKey.isEmpty()) {
            this.enable = ConfigUtil.getConfig().getBoolean(enableKey);
        } else {
            this.enable = true;
        }
    }

    public String onPlaceholder(OfflinePlayer player, @NotNull String prams) {
        if (!isEnable()) {
            return null;
        }
        return placeholder(player, prams);
    }
}
