package cache;

import com.google.common.cache.Cache;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utils.Constants.EXP_MINUTES_10;
import static utils.Constants.EXP_MINUTES_120;
import static utils.Constants.EXP_MINUTES_125;
import static utils.Constants.EXP_MINUTES_20;

public class CacheProviderTest {

    @Before
    public void setUp() {
        CacheProvider.clearAllCache();
    }

    @Test
    public void testGetCache() {

        final Cache<String, Object> cacheMinutes10 =
                CacheProvider.getCache(EXP_MINUTES_10);

        assertNotNull("Cache value null",
                        cacheMinutes10);
        assertTrue("Cache store size 0",
                CacheProvider.size() > 0);
    }

    @Test
    public void testClearCacheByKey() {
        final Cache<String, Object> cacheMinutes10 =
                CacheProvider.getCache(EXP_MINUTES_10);

        assertNotNull("Cache value null",
                        cacheMinutes10);
        assertTrue("Cache store size 0",
                    CacheProvider.size() > 0);

        CacheProvider.clearCacheByKey(-1);
        assertTrue("Cache store size 0",
                CacheProvider.size() > 0);

        CacheProvider.clearCacheByKey(EXP_MINUTES_10);
        assertEquals("Cache store size greater than 0",
                    0,
                            CacheProvider.size());
    }

    @Test
    public void clearAllCache() {

        final Cache<String, Object> cacheMinutes10 =
                CacheProvider.getCache(EXP_MINUTES_10);

        assertNotNull("Cache value null",
                cacheMinutes10);
        assertTrue("Cache store size 0",
                CacheProvider.size() > 0);

        CacheProvider.clearCacheByKey(-1);
        assertTrue("Cache store size 0",
                CacheProvider.size() > 0);

        CacheProvider.clearAllCache();
        assertEquals("Cache store size greater than 0",
                0,
                CacheProvider.size());
    }

    @Test
    public void testExpirationTimeConstants() {

        assertEquals("Duration is not 10",
                10,
                EXP_MINUTES_10);

        assertEquals("Duration is not 20",
                20,
                EXP_MINUTES_20);

        assertEquals("Duration is not 120",
                120,
                EXP_MINUTES_120);

        assertEquals("Duration is not 125",
                125,
                EXP_MINUTES_125);
    }
}