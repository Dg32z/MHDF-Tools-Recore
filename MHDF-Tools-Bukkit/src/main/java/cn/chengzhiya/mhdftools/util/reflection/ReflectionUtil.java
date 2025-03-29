package cn.chengzhiya.mhdftools.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtil {
    /**
     * 通过反射指定类实例获取指定方法的方法实例
     *
     * @param clazz      类实例
     * @param methodName 方法名称
     * @param accessible 强制访问
     * @param argsTypes  传参类型
     * @return 方法实例
     */
    public static Method getMethod(Class<?> clazz, String methodName, boolean accessible, Class<?>... argsTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, argsTypes);
            method.setAccessible(accessible);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过反射指定方法实例获取返回值
     *
     * @param method 方法实例
     * @param object 对象实例
     * @param args   传入参数
     * @return 返回值
     */
    public static <T> T invokeMethod(Method method, Object object, Object... args) {
        try {
            Object invokeObject = method.invoke(object, args);
            if (invokeObject == null) {
                return null;
            }
            return (T) invokeObject;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
