package pks;

import cache.CacheProvider;
import org.junit.Before;
import org.junit.Test;

public class AppTest {

    @Before
    public void setUp() {
        CacheProvider.clearAllCache();
    }

    @Test
    public void testApp() {
        App.main(new String[0]);
    }
}