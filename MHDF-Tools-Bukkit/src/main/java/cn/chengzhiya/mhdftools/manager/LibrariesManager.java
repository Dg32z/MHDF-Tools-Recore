package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.interfaces.Init;
import cn.chengzhiya.mhdftools.libraries.Dependency;
import cn.chengzhiya.mhdftools.libraries.DependencyManager;
import cn.chengzhiya.mhdftools.libraries.DependencyManagerImpl;
import cn.chengzhiya.mhdftools.libraries.classpath.ReflectionClassPathAppender;

import java.util.List;

@SuppressWarnings("unused")
public final class LibrariesManager implements Init {
    /**
     * 下载并加载所有所需依赖
     */
    @Override
    public void init() {
        DependencyManager dependencyManager = new DependencyManagerImpl(
                new ReflectionClassPathAppender(Main.class.getClassLoader())
        );

        dependencyManager.loadDependencies(List.of(Dependency.values()));
    }
}
