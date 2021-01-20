package cn.monkeyapp.mavd.cache;

import java.util.Collection;
import java.util.Map;

/**
 * 缓存接口
 *
 * @author Corbett Zhang
 */
public interface ICache {

    /**
     * 获取缓存名称
     *
     * @return 缓存名称
     */
    String getName();

    /**
     * 是否存在指定key
     *
     * @param keys 缓存key
     * @return bool值
     */
    boolean contains(String... keys);

    /**
     * 添加缓存
     *
     * @param value 缓存值
     * @param keys  缓存key
     */
    void add(Object value, String... keys);

    /**
     * 添加缓存并设置超时时间
     *
     * @param value  缓存值
     * @param second 超时时间
     * @param keys   缓存key
     */
    void add(Object value, int second, String... keys);

    /**
     * 获取缓存值
     *
     * @param keys 缓存key
     * @return 缓存值
     */
    Object get(String... keys);

    /**
     * 获取指定类型的缓存值
     *
     * @param clazz 缓存类
     * @param keys  缓存key
     * @param <T>   类型
     * @return 缓存值
     */
    <T> T get(Class<T> clazz, String... keys);

    /**
     * 删除缓存
     *
     * @param keys 缓存key
     */
    void remove(String... keys);

    /**
     * 清空缓存
     */
    void clear();

    /**
     * 添加缓存
     *
     * @param key    缓存key
     * @param values map类型缓存值
     */
    void add(String key, Map<String, Map<String, Object>> values);

    /**
     * 添加缓存数据并设置过期时间
     *
     * @param key    缓存key
     * @param values 缓存值
     * @param second 过期时间
     */
    void add(String key, Map<String, Map<String, Object>> values, int second);

    /**
     * 获取缓存数据
     *
     * @param clazz  类对象
     * @param key    缓存key
     * @param fields 缓存值
     * @param <T>    返回类型
     * @return 缓存值
     */
    <T> Map<String, T> get(Class<T> clazz, String key, Collection<String> fields);

    /**
     * 删除缓存中集合的某些值
     *
     * @param key    缓存key
     * @param fields 缓存值集合
     */
    void remove(String key, Collection<String> fields);

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    void removeByKey(String key);
}
