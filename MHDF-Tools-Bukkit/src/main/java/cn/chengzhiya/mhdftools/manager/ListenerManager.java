package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.interfaces.Init;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public final class ListenerManager implements Init {
    /**
     * 注册所有启用的监听器
     */
    @Override
    public void init() {
        try {
            Reflections reflections = new Reflections(AbstractListener.class.getPackageName());

            for (Class<? extends AbstractListener> clazz : reflections.getSubTypesOf(AbstractListener.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractListener abstractListener = clazz.getDeclaredConstructor().newInstance();
                    if (abstractListener.isEnable()) {
                        Bukkit.getPluginManager().registerEvents(abstractListener, Main.instance);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
