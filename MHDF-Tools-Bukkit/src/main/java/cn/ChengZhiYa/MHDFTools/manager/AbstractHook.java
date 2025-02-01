package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.interfaces.Hook;
import lombok.Getter;

@Getter
public abstract class AbstractHook implements Hook {
    public boolean enable = false;
}
