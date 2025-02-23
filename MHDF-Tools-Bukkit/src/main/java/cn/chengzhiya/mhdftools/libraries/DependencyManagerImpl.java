package cn.ChengZhiYa.MHDFTools.libraries;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.libraries.classpath.ClassPathAppender;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.FileUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.EnumMap;
import java.util.concurrent.CountDownLatch;

public final class DependencyManagerImpl implements DependencyManager {
    private final Path dependenciesFolder;
    private final ClassPathAppender classPathAppender;
    private final EnumMap<Dependency, Path> loaded = new EnumMap<>(Dependency.class);

    public DependencyManagerImpl(ClassPathAppender classPathAppender) {
        this.dependenciesFolder = setupDependenciesFolder();
        this.classPathAppender = classPathAppender;
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

        Path file = downloadDependency(dependency);

        this.loaded.put(dependency, file);

        Main.instance.getLogger().info("正在加载依赖 " + dependency.getFileName());
        this.classPathAppender.addJarToClasspath(file);
        Main.instance.getLogger().info("依赖 " + dependency.getFileName() + " 加载完成!");
    }

    /**
     * 下载指定依赖信息实例对应的依赖
     *
     * @param dependency 依赖信息实例
     * @return 依赖的文件路径
     */
    private Path downloadDependency(Dependency dependency) {
        Path file = dependenciesFolder.resolve(dependency.getFileName());

        if (Files.exists(file)) {
            return file;
        }

        String fileName = dependency.getFileName();
        Repository repository = dependency.getRepository();

        Main.instance.getLogger().info("正在下载依赖 " + fileName + "(" + repository.getUrl() + dependency.getMavenRepoPath() + ")");
        repository.download(dependency, file);
        Main.instance.getLogger().info("依赖 " + fileName + " 下载完成!");
        return file;
    }
}
