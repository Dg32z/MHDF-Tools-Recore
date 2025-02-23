package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.interfaces.Init;
import cn.chengzhiya.mhdftools.task.AbstractTask;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public final class TaskManager implements Init {
    /**
     * 注册所有启用的计划任务
     */
    @Override
    public void init() {
        try {
            Reflections reflections = new Reflections(AbstractTask.class.getPackageName());

            for (Class<? extends AbstractTask> clazz : reflections.getSubTypesOf(AbstractTask.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractTask abstractTask = clazz.getDeclaredConstructor().newInstance();
                    if (abstractTask.isEnable()) {
                        abstractTask.runTaskTimerAsynchronously(Main.instance, 0L, abstractTask.getTime());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
