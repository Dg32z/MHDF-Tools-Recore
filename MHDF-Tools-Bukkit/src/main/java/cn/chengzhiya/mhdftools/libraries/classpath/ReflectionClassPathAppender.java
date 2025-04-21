package cn.chengzhiya.mhdftools.libraries.classpath;

import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.nio.file.Path;

public final class ReflectionClassPathAppender implements ClassPathAppender {
    private final URLClassLoaderAccess classLoaderAccess;

    public ReflectionClassPathAppender(ClassLoader classLoader) throws IllegalStateException {
        if (classLoader instanceof URLClassLoader) {
            this.classLoaderAccess = URLClassLoaderAccess.create((URLClassLoader) classLoader);
        } else {
            throw new RuntimeException("classLoader 类型并不是 URLClassLoader");
        }
    }

    /**
     * 加载指定jar文件到classpath中
     *
     * @param file jar文件路径
     */
    @Override
    public void addJarToClasspath(Path file) {
        try {
            this.classLoaderAccess.addURL(file.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
