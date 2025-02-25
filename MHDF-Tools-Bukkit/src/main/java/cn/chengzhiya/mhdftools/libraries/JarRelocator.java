package cn.chengzhiya.mhdftools.libraries;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

public class JarRelocator {
    private static final String JAR_RELOCATOR_CLASS = "me.lucko.jarrelocator.JarRelocator";
    private static final String JAR_RELOCATOR_RUN_METHOD = "run";

    private final Constructor<?> jarRelocatorConstructor;
    private final Method jarRelocatorRunMethod;

    public JarRelocator() {
        try {
            Class<?> jarRelocatorClass = Class.forName(JAR_RELOCATOR_CLASS);
            this.jarRelocatorConstructor = jarRelocatorClass.getDeclaredConstructor(File.class, File.class, Map.class);
            this.jarRelocatorConstructor.setAccessible(true);
            this.jarRelocatorRunMethod = jarRelocatorClass.getDeclaredMethod(JAR_RELOCATOR_RUN_METHOD);
            this.jarRelocatorRunMethod.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 重新映射JAR文件中的类路径并生成新的JAR文件
     *
     * @param input       需要处理的目标JAR文件对象
     * @param output      重定位后生成的JAR文件对象
     * @param relocations 类路径重定位映射规则，键为原始路径，值为目标路径
     * @throws Exception 可能抛出的反射操作异常或文件操作异常
     */
    public void remap(File input, File output, Map<String, String> relocations) throws Exception {
        Object jarRelocator = this.jarRelocatorConstructor.newInstance(input, output, relocations);
        this.jarRelocatorRunMethod.invoke(jarRelocator);
    }
}