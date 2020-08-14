package com.mashirro.framework.cache;


import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 简单缓存，无超时实现，默认使用{@link WeakHashMap}实现缓存自动清理
 */
public class SimpleCache<K, V> implements Iterable<Map.Entry<K, V>>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 池
     */
    private final Map<K, V> cache;

    /**
     * 乐观读写锁
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    /**
     * 构造，默认使用{@link WeakHashMap}实现缓存自动清理
     */
    public SimpleCache() {
        this.cache = new WeakHashMap<>();
    }

    /**
     * 从缓存池中查找值
     *
     * @param key 键
     * @return 值
     */
    public V get(K key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }


    /**
     * 放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 值
     */
    public V put(K key, V value) {
        //独占写锁
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
        return value;
    }

    /**
     * 移除缓存
     *
     * @param key 键
     * @return 移除的值
     */
    public V remove(K key) {
        // 独占写锁
        lock.writeLock().lock();
        try {
            return cache.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }


    /**
     * 清空缓存池
     */
    public void clear() {
        // 独占写锁
        lock.writeLock().lock();
        try {
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }


    /**
     * 返回类型为Map.Entry<K, V>的迭代器。
     * @return
     */
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return cache.entrySet().iterator();
    }


    /**
     * 测试main方法
     * @param args
     */
    public static void main(String[] args) {
        SimpleCache<String, Object> simpleCache = new SimpleCache<>();
        simpleCache.put("name","tom");
        simpleCache.put("age",13);
        Iterator<Map.Entry<String, Object>> iterator = simpleCache.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
