package cn.chengzhiya.mhdftools.hook;

import cn.chengzhiya.mhdftools.interfaces.Hook;
import lombok.Getter;

@Getter
public abstract class AbstractHook implements Hook {
    public boolean enable = false;
}
