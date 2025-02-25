package cn.chengzhiya.mhdftools.entity;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import lombok.Getter;

@Getter
public abstract class AbstractDao {
    private final boolean enable;

    public AbstractDao(String enableKey) {
        if (enableKey != null && !enableKey.isEmpty()) {
            this.enable = ConfigUtil.getConfig().getBoolean(enableKey);
        } else {
            this.enable = true;
        }
    }
}
