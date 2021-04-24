package cn.monkeyapp.mavd.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 本地缓存，单例模式
 *
 * @author Corbett Zhang
 */
public class LocalCache implements ICache {

    private LocalCache() {
        super();
        this.cache = new ConcurrentHashMap<>(100);
    }

    private static class SingletonContainer {
        private static LocalCache instance = new LocalCache();
    }

    public static LocalCache getInstance() {
        return SingletonContainer.instance;
    }

    protected Map<String, Object> cache;

    @Override
    public void add(Object value, String... keys) {
        add(value, 0, keys);
    }

    @Override
    public void add(Object value, int seconds, String... keys) {
        String key = genKey(keys);
        if (value != null) {
            cache.put(key, value);
        }
    }

    @Override
    public boolean contains(String... keys) {
        String key = genKey(keys);
        return cache.containsKey(key);
    }

    @Override
    public Object get(String... keys) {
        String key = genKey(keys);
        return cache.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Class<T> clazz, String... keys) {
        return (T) this.get(keys);
    }

    @Override
    public void remove(String... keys) {
        String key = genKey(keys);
        cache.remove(key);
    }

    protected String genKey(String... keys) {
        Object[] args = new Object[keys.length];

        int i = 0;
        for (Object key : keys) {
            args[i++] = key;
        }

        return getKey(args);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    protected Set<String> getAllKeys() {
        return cache.keySet();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void add(String key, Map<String, Map<String, Object>> values) {
    }

    @Override
    public void add(String key, Map<String, Map<String, Object>> values, int second) {
    }

    @Override
    public <T> Map<String, T> get(Class<T> clazz, String key, Collection<String> fields) {
        return null;
    }

    @Override
    public void remove(String key, Collection<String> fields) {
    }

    @Override
    public void removeByKey(String key) {
    }

    private String getKey(Object... args) {
        return getKeyBySplit(args);
    }

    private String getKeyBySplit(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg);
            sb.append("_");
        }

        int length = sb.length();
        if (length > 0) {
            sb.delete(length - "_".length(), length);
        }

        return sb.toString();
    }

    public Map<String, Object> parseMapForFilter(String filters) {
        if (cache == null) {
            return null;
        } else {
            cache = cache.entrySet().stream()
                    .filter((e) -> e.getKey().contains(filters))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                    ));
        }
        return cache;
    }
}
