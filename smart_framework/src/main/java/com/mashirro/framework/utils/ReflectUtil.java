package com.mashirro.framework.utils;

import com.sun.xml.internal.ws.util.UtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 创建实例
     *
     * @param clazz 类
     * @param <T>   对象类型
     * @return
     */
    public static <T> T newInstance(Class<T> clazz) {
        T instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            logger.error("创建实例失败!", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return instance;
    }


    /**
     * 执行方法
     *
     * @param obj    对象，如果执行静态方法，此值为null
     * @param method 方法（对象方法或static方法都可）
     * @param args   参数对象
     * @param <T>    返回对象类型
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object obj, Method method, Object... args) throws Exception {
        //设置方法为可访问
        setAccessible(method);
        try {
            return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 设置对象为可访问
     *
     * @param <T>              AccessibleObject的子类，比如Class、Method、Field等
     * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
     * @return 被设置可访问的对象
     */
    public static <T extends AccessibleObject> T setAccessible(T accessibleObject) {
        if (accessibleObject != null && !accessibleObject.isAccessible()) {
            accessibleObject.setAccessible(true);
        }
        return accessibleObject;
    }


    /**
     * 设置字段值
     *
     * @param obj   对象
     * @param field 字段
     * @param value 值
     * @throws UtilException
     */
    public static void setFieldValue(Object obj, Field field, Object value) throws Exception {
        //设置字段为可访问
        setAccessible(field);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
