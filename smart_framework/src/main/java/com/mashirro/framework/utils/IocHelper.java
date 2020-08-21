package com.mashirro.framework.utils;


import com.mashirro.framework.annotation.Inject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * 依赖注入助手类
 */
public class IocHelper {


    static {
        //获取所有的Bean类与Bean实例之间的映射关系
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (beanMap != null && !beanMap.isEmpty()) {
            //遍历beanMap
            Set<Map.Entry<Class<?>, Object>> entries = beanMap.entrySet();
            for (Map.Entry<Class<?>, Object> entry : entries) {
                //bean Class
                Class<?> beanClass = entry.getKey();
                Field[] beanFields = beanClass.getDeclaredFields();
                for (Field beanField : beanFields) {
                    //如果beanField带有Inject注解
                    if (beanField.isAnnotationPresent(Inject.class)) {
                        Object beanFieldInstance = beanMap.get(beanField.getType());
                        //进行依赖注入
                        if (beanFieldInstance != null) {
                            ReflectUtil.setFieldValue(entry.getValue(), beanField, beanFieldInstance);
                        }
                    }
                }
            }
        }
    }
}
