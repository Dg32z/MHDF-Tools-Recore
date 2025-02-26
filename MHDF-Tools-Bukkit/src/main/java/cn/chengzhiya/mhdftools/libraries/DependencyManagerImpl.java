package cn.chengzhiya.mhdftools.libraries;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.exception.FileException;
import cn.chengzhiya.mhdftools.libraries.classpath.ClassPathAppender;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.FileUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public final class DependencyManagerImpl implements DependencyManager {
    private static final String RELOCATION_PREFIX = "cn.chengzhiya.mhdftools.libs.";

    private final Path dependenciesFolder;
    private final ClassPathAppender classPathAppender;
    private final EnumMap<Dependency, Path> loaded = new EnumMap<>(Dependency.class);
    public JarRelocator jarRelocator;

    public DependencyManagerImpl(ClassPathAppender classPathAppender) {
        this.dependenciesFolder = setupDependenciesFolder();
        this.classPathAppender = classPathAppender;
        this.jarRelocator = null;
    }

    /**
     * 初始化依赖文件夹
     *
     * @return 依赖文件夹路径
     */
    private static Path setupDependenciesFolder() {
        File file = new File(ConfigUtil.getDataFolder(), "libs");
        try {
            FileUtil.createFolder(file);
        } catch (FileException e) {
            throw new RuntimeException(e);
        }
        return file.toPath();
    }

    /**
     * 初始化 JarRelocator
     */
    public void init() {
        jarRelocator = new JarRelocator();
    }

    /**
     * 下载指定依赖信息实例列表对应的全部依赖
     *
     * @param dependencies 依赖信息实例列表
     */
    @Override
    public void downloadDependencies(Collection<Dependency> dependencies) {
        CountDownLatch latch = new CountDownLatch(dependencies.size());

        for (Dependency dependency : dependencies) {
            if (this.loaded.containsKey(dependency)) {
                latch.countDown();
                continue;
            }

            try {
                downloadDependency(dependency);
            } catch (Throwable e) {
                throw new RuntimeException("无法下载依赖 " + dependency, e);
            } finally {
                latch.countDown();
            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * 加载指定依赖信息实例列表对应的全部依赖
     *
     * @param dependencies 依赖信息实例列表
     */
    @Override
    public void loadDependencies(Collection<Dependency> dependencies) {
        CountDownLatch latch = new CountDownLatch(dependencies.size());

        for (Dependency dependency : dependencies) {
            if (this.loaded.containsKey(dependency)) {
                latch.countDown();
                continue;
            }

            try {
                loadDependency(dependency);
            } catch (Throwable e) {
                throw new RuntimeException("无法下载依赖 " + dependency, e);
            } finally {
                latch.countDown();
            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 加载指定依赖信息实例对应的依赖
     *
     * @param dependency 依赖信息实例
     */
    private void loadDependency(Dependency dependency) {
        if (this.loaded.containsKey(dependency)) {
            return;
        }

        Path file = remapDependency(dependency);

        this.loaded.put(dependency, file);

        Main.instance.getLogger().info("正在加载依赖 " + dependency.getFileName());
        this.classPathAppender.addJarToClasspath(file);
        Main.instance.getLogger().info("依赖 " + dependency.getFileName() + " 加载完成!");
    }

    /**
     * 下载指定依赖信息实例对应的依赖
     *
     * @param dependency 依赖信息实例
     */
    private void downloadDependency(Dependency dependency) {
        Path file = dependenciesFolder.resolve(dependency.getFileName());

        if (Files.exists(file)) {
            return;
        }

        String fileName = dependency.getFileName();
        Repository repository = dependency.getRepository();
        Main.instance.getLogger().info("正在下载依赖 " + fileName + "(" + repository.getUrl() + dependency.getMavenRepoPath() + ")");
        repository.download(dependency, file);
    }

    /**
     * 重定位依赖
     *
     * @param dependency 依赖信息实例
     * @return 重定位后的依赖路径
     */
    private Path remapDependency(Dependency dependency) {
        Path file = dependenciesFolder.resolve(dependency.getFileName());

        if (!isRelocatable(dependency)) {
            return file;
        }

        if (Files.exists(getRelocatedPath(file))) {
            return getRelocatedPath(file);
        }

        try {
            jarRelocator.remap(
                    file.toFile(), getRelocatedPath(file).toFile(),
                    Map.of(dependency.getGroupId(), RELOCATION_PREFIX + dependency.getGroupId())
            );
            return getRelocatedPath(file);
        } catch (Exception e) {
            throw new RuntimeException("重定位依赖失败: " + dependency.getFileName(), e);
        }
    }

    /**
     * 获取重定位后的依赖路径
     *
     * @param original 原始依赖路径
     * @return 重定位后的依赖路径
     */
    private Path getRelocatedPath(Path original) {
        return original.resolveSibling("relocated-" + original.getFileName());
    }

    /**
     * 判断依赖是否需要重定位
     *
     * @param dependency 依赖信息实例
     * @return 结果
     */
    public boolean isRelocatable(Dependency dependency) {
        return dependency != Dependency.JAR_RELOCATOR &&
                dependency != Dependency.ASM &&
                dependency != Dependency.ASM_COMMONS &&
                dependency != Dependency.H2_DRIVER &&
                dependency != Dependency.MYSQL_DRIVER &&
                dependency != Dependency.REACTOR_CORE &&
                dependency != Dependency.REACTIVE_STREAMS;
    }
}
