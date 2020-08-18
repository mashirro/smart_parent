package com.mashirro.framework.utils;


/**
 * 声明为函数式接口
 * Filter:过滤器接口
 */
@FunctionalInterface
public interface Filter<T> {

    /**
     * 是否接受对象
     *
     * @param t 检查的对象
     * @return 是否接受对象
     */
    boolean accept(T t);
}
