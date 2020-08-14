package com.mashirro.framework.utils;


import com.mashirro.framework.cache.SimpleCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类加载工具类
 */
public class ClassLoaderUtil {

    private static final Logger logger = LoggerFactory.getLogger(ClassLoaderUtil.class);

    private static final SimpleCache<String, Class<?>> classCache = new SimpleCache<>();


    /**
     * 获取当前线程的类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    /**
     * 加载类,通过传入类的字符串,返回其对应的类名
     *
     * @param name 类名
     * @return
     * @throws Exception
     */
    public static Class<?> loadClass(String name) throws Exception {
        return loadClass(name, null, true);
    }


    /**
     * 加载类,通过传入类的字符串,返回其对应的类名
     * 此方法支持缓存，第一次被加载的类之后会读取缓存中的类
     *
     * @param name          类名
     * @param classLoader   类加载器
     * @param isInitialized 是否初始化类(调用static模块内容和初始化static属性)
     * @return 类名对应的类
     * @throws Exception
     */
    public static Class<?> loadClass(String name, ClassLoader classLoader, boolean isInitialized) throws Exception {
        if (StringUtils.isEmpty(name)) {
            logger.error("Name must not be empty");
            throw new IllegalArgumentException("Name must not be empty");
        }
        // 加载缓存中的类
        Class<?> clazz = classCache.get(name);
        if (clazz != null) {
            return clazz;
        }

        if (classLoader == null) {
            classLoader = getClassLoader();
        }
        //加载类
        clazz = Class.forName(name, isInitialized, classLoader);
        // 加入缓存并返回
        return classCache.put(name, clazz);
    }


    /**
     * 测试main方法
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Class<?> clazz = loadClass("com.mashirro.framework.pojo.ConfigConstants");
        System.out.println(clazz);
    }
}
