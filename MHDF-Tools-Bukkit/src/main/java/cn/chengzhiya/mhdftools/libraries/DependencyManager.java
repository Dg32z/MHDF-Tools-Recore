package cn.chengzhiya.mhdftools.libraries;

import java.util.Collection;

public interface DependencyManager {
    void downloadDependencies(Collection<Dependency> dependencies);

    void loadDependencies(Collection<Dependency> dependencies);

    void init();
}
