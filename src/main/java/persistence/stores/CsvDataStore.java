package persistence.stores;

import cache.CacheProvider;
import com.google.common.cache.Cache;
import exceptions.DataStoreException;
import io.DelimitedReader;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static utils.Constants.EXCLUDE_CSV_HEADERS;
import static utils.Constants.EXP_MINUTES_120;

/**
 * An implementation of Csv data store. The data is stored in
 * a cache for fast retrieval.
 * <br>
 * <b>Note:</b> Default cache expiration 120 minutes
 * <br><br>
 * This implementation uses google guava library for caching.
 * <br>
 * <a href="https://github.com/google/guava/wiki/CachesExplained">CachesExplained</a>
 *
 * @version 1.0
 * @since 1.0
 * @see Cache
 * Date: 27-feb-2021
 *
 */
public class CsvDataStore
        implements DataStore<Boolean, List<List<String>>> {

    private final DelimitedReader<Path, List<List<String>>> reader;
    private final Path filePath;
    private final Cache<String, Object> cache;

    private static final String CSV_DATA = "csv-data-key";

    public CsvDataStore(DelimitedReader<Path, List<List<String>>> reader,
                        Path filePath) {
        this.reader = reader;
        this.filePath = filePath;

        this.cache = CacheProvider.getCache(EXP_MINUTES_120);
    }

    /**
     * Retrieves data from Csv reader.
     * The data stored into a cache for fast retrieval,
     * if not exists already.
     *
     * The Csv data can be retrieved directly from file by
     * disabling cached option during invocation.
     * <br>
     * <b>Note:</b>Disabling cache option is resource intensive
     * and slower depending on the size of the data.
     *
     * @param cached Indicates whether to retrieve data from cache or file
     * @return A list of rows and columns
     * @throws DataStoreException Any exception thrown by caching provider
     *                            will be wrapped in this exception.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<List<String>> getData(Boolean cached)
            throws DataStoreException {

        // clears existing data from cache
        if (!cached) {
            this.cache.invalidate(CSV_DATA);
        }

        try {

            return (List<List<String>>) this.cache.get(
                                CSV_DATA,
                                (Callable<List<List<String>>>) () -> reader.read(filePath, EXCLUDE_CSV_HEADERS)
                    );

        } catch (ExecutionException e) {
            throw new DataStoreException(e);
        }
    }
}
