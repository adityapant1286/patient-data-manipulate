package pks.grouping;

import cache.CacheProvider;
import com.google.common.cache.Cache;
import exceptions.GroupingProviderException;
import persistence.models.PatientAttribute;
import persistence.stores.DataStore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static java.util.stream.Collectors.groupingBy;
import static utils.Constants.EXP_MINUTES_20;

/**
 * An implementation of grouping data by episode.
 * An episode is a group of all attributes for one patient on a single day.
 * The data is stored in a cache for fast retrieval.
 * <br>
 * <b>Note:</b> Default cache expiration 20 minutes
 * <br><br>
 * This implementation uses google guava library for caching.
 * <br>
 * <a href="https://github.com/google/guava/wiki/CachesExplained">CachesExplained</a>
 *
 * @version 1.0
 * @since 1.0
 * @see Cache
 * Date: 28-feb-2021
 *
 */
public class EpisodeGroupingProvider
        implements GroupingProvider<Map<GroupingKey, List<PatientAttribute>>> {

    private static final String GROUPED_BY_EPISODE_KEY = "grouped-by-episode-key";

    private final Cache<String, Object> cache;
    private final DataStore<Boolean, List<List<String>>> csvDataStore;
    private final DataStore<List<List<String>>, List<PatientAttribute>> patientDataStore;

    public EpisodeGroupingProvider(DataStore<Boolean, List<List<String>>> csvDataStore,
                                   DataStore<List<List<String>>, List<PatientAttribute>> patientDataStore) {
        this.csvDataStore = csvDataStore;
        this.patientDataStore = patientDataStore;
        this.cache = CacheProvider.getCache(EXP_MINUTES_20);
    }

    /**
     * Groups data by Date and Patient Id
     *
     * @return Data grouped by Episode
     * @throws GroupingProviderException when there is an anomaly
     *                                   found during grouping operation
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<GroupingKey, List<PatientAttribute>> getGroupedData()
            throws GroupingProviderException {

        try {
            return (Map<GroupingKey, List<PatientAttribute>>) this.cache.get(GROUPED_BY_EPISODE_KEY,
                    (Callable<Map<GroupingKey, List<PatientAttribute>>>) () -> {
                        final List<List<String>> csvData = csvDataStore.getData(true);
                        final List<PatientAttribute> patientAttributes = patientDataStore.getData(csvData);

                        return patientAttributes.stream()
                                .collect(
                                        groupingBy(a -> new GroupingKey(a.getDate(), a.getPatientId()))
                                );
                    }
            );
        } catch (Exception e) {
            throw new GroupingProviderException(e);
        }

    }
}
