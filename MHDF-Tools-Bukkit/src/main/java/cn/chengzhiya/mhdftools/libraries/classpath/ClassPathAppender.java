package cn.ChengZhiYa.MHDFTools.libraries.classpath;

import java.nio.file.Path;

public interface ClassPathAppender {
    void addJarToClasspath(Path file);
}
