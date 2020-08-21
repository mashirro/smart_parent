package com.mashirro.framework.utils;


import com.mashirro.framework.annotation.Controller;
import com.mashirro.framework.annotation.Service;
import com.mashirro.framework.pojo.ConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Bean助手类(相当于一个Bean容器)
 */
public class BeanHelper {

    private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    /**
     * BEAN_MAP:用于存放Bean类与Bean实例的映射关系
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    /**
     * 定义类集合,用于存放所加载的类
     */
    private static final Set<Class<?>> BEAN_CLASS_SET = new HashSet<>();

    //静态代码块
    static {
        //扫描指定包路径下包含Controller和Service注解的类
        BEAN_CLASS_SET.addAll(ClassUtil.scanPackageByAnnotation(ConfigConstants.APP_BASE_PACKAGE, Controller.class));
        BEAN_CLASS_SET.addAll(ClassUtil.scanPackageByAnnotation(ConfigConstants.APP_BASE_PACKAGE, Service.class));
        for (Class<?> beanClass : BEAN_CLASS_SET) {
            //创建实例
            Object o = ReflectUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass, o);
        }
    }


    /**
     * 获取BEAN_MAP
     *
     * @return
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }


    /**
     * 获取Bean实例
     *
     * @param clazz class对象
     * @param <T>   Bean实例
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("获取Bean实例出现异常!");
        } else {
            return ((T) BEAN_MAP.get(clazz));
        }
    }

}
