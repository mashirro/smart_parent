package com.mashirro.framework.utils;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * 类处理工具
 * 这个工具主要是封装了一些反射的方法，使调用更加方便。
 * 而这个类中最有用的方法是scanPackage方法，这个方法会扫描classpath下所有类，这个在Spring中是特性之一。
 */
public class ClassUtil {

    /**
     * 扫描指定包路径下所有包含指定注解的类
     *
     * @param packageName
     * @param annotationClass
     * @return
     */
    public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) throws IOException {
//        return new ClassScanner(packageName, new Filter<Class<?>>() {
//            @Override
//            public boolean accept(Class<?> clazz) {
//                //如果此元素上存在指定annotation 类型的annotation，则为true，否则为false
//                return clazz.isAnnotationPresent(annotationClass);
//            }
//        }).scan();

        //可使用lambda表达式
        return new ClassScanner(packageName, clazz -> clazz.isAnnotationPresent(annotationClass)).scan();
    }


    /**
     * 扫描指定包路径下所有类
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> scanPackage(String packageName) throws IOException {
        return new ClassScanner(packageName, null).scan();
    }


    /**
     * 根据类型获取默认值,规则如下:
     * 1.如果为原始类型，返回0
     * 2.非原始类型返回null
     *
     * @param clazz 类
     * @return 默认值
     */
    public static Object getDefaultValue(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (long.class == clazz) {
                return 0L;
            } else if (int.class == clazz) {
                return 0;
            } else if (short.class == clazz) {
                return (short) 0;
            } else if (char.class == clazz) {
                return (char) 0;
            } else if (byte.class == clazz) {
                return (byte) 0;
            } else if (double.class == clazz) {
                return 0D;
            } else if (float.class == clazz) {
                return 0f;
            } else if (boolean.class == clazz) {
                return false;
            }
        }
        return null;
    }

    /**
     * 是否为静态方法
     *
     * @param method 方法
     * @return 是否为静态方法
     */
    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

}
