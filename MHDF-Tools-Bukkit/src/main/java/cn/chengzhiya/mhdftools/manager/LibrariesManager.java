package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.interfaces.Init;
import cn.ChengZhiYa.MHDFTools.libraries.Dependency;
import cn.ChengZhiYa.MHDFTools.libraries.DependencyManager;
import cn.ChengZhiYa.MHDFTools.libraries.DependencyManagerImpl;
import cn.ChengZhiYa.MHDFTools.libraries.classpath.ReflectionClassPathAppender;

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
