package com.mashirro.framework.utils;


import java.io.IOException;
import java.lang.annotation.Annotation;
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
        return new ClassScanner(packageName, new Filter<Class<?>>() {
            @Override
            public boolean accept(Class<?> clazz) {
                //如果此元素上存在指定annotation 类型的annotation，则为true，否则为false
                return clazz.isAnnotationPresent(annotationClass);
            }
        }).scan();
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


}
