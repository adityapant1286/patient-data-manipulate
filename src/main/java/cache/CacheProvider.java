package cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * A centralised implementation of cache store.
 *
 * This is a final class therefore cannot be overridden and having
 * a private constructor the caller has access to static methods.
 *
 * @version 1.0
 * @since 1.0
 * Date: 28-feb-2021
 */
public final class CacheProvider {

    private CacheProvider() {}

    private static final Map<Integer, Cache<String, Object>> cacheStore =
            new ConcurrentHashMap<>();

    /**
     * Retrieves associated cache by a key.
     * If the key is not present already then a new value
     * will be computed and return to caller.
     *
     * @param minutesKey {@link Integer}
     * @return {@link Cache} a cache associated with the key
     * @see Cache
     */
    public static Cache<String, Object> getCache(Integer minutesKey) {

        return cacheStore.computeIfAbsent(minutesKey, c -> CacheBuilder.newBuilder()
                                                            .expireAfterWrite(minutesKey, TimeUnit.MINUTES)
                                                            .build());
    }

    /**
     * If present, first the associated cache will be cleared by key,
     * then the entry from store will be removed.
     *
     * @param minutesKey {@link Integer} a key of a cache to be cleared
     */
    public static void clearCacheByKey(Integer minutesKey) {

        cacheStore.computeIfPresent(minutesKey,(k, v) -> {
            v.invalidateAll();
            return null;
        });

        cacheStore.remove(minutesKey);
    }

    /**
     * Clears all associated caches from the store, before emptying
     * the store.
     */
    public static void clearAllCache() {
        cacheStore.values().forEach(Cache::invalidateAll);
        cacheStore.clear();
    }

    /**
     * The total number of caches ini store.
     *
     * <b>Note:</b>
     * Call {@code stats()} method on individual cache to view
     * the status of each cache.
     *
     * @return cache store size
     */
    public static int size() {
        return cacheStore.size();
    }
}
