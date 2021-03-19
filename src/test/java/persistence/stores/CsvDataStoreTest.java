package persistence.stores;

import cache.CacheProvider;
import exceptions.DataStoreException;
import io.CsvReaderImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CsvDataStoreTest {

    private DataStore<Boolean, List<List<String>>> dataStore;
    private Path csvFilePath;
    @Before
    public void setUp()
            throws Exception {
        final URI csvFileUri = ClassLoader.getSystemResource("messages.csv")
                                            .toURI();
        this.csvFilePath = Paths.get(csvFileUri);

        this.dataStore = new CsvDataStore(new CsvReaderImpl(),
                                            csvFilePath);
        CacheProvider.clearAllCache();
    }

    @After
    public void tearDown() {
        this.csvFilePath = null;
        this.dataStore = null;
    }

    @Test
    public void testCachedAndNonCachedData()
            throws Exception {
        final long startNonCached = System.currentTimeMillis();
        final List<List<String>> nonCachedData = this.dataStore.getData(false);
        final long timeTakenNonCached = System.currentTimeMillis() - startNonCached;

        assertFalse("Data is empty",
                    nonCachedData.isEmpty());

        final long startCached = System.currentTimeMillis();

        final List<List<String>> cachedData = this.dataStore.getData(true);
        final long timeTakenCached = System.currentTimeMillis() - startCached;

        assertFalse("Data is empty",
                    cachedData.isEmpty());

        assertTrue("Cached option taken more time",
                timeTakenCached < timeTakenNonCached);
    }

    @Test(expected = DataStoreException.class)
    public void textDataStoreException()
            throws DataStoreException {

        throw new DataStoreException();
    }

    @Test(expected = DataStoreException.class)
    public void textDataStoreExceptionMessage()
            throws DataStoreException {

        throw new DataStoreException("test");
    }
    @Test(expected = DataStoreException.class)
    public void textDataStoreExceptionThrowable()
            throws DataStoreException {

        throw new DataStoreException(new Exception());
    }
}