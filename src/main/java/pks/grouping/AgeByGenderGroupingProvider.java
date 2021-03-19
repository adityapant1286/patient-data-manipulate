package pks.grouping;

import cache.CacheProvider;
import com.google.common.cache.Cache;
import exceptions.GroupingProviderException;
import persistence.models.PatientAttribute;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static functional.DistinctPredicate.distinctByKey;
import static utils.Constants.EXP_MINUTES_10;

/**
 * An implementation of grouping data by gender and age.
 * The data is stored in a cache for fast retrieval.
 * <br>
 * <b>Note:</b> Default cache expiration 10 minutes
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
public class AgeByGenderGroupingProvider
        implements GroupingProvider<List<List<PatientAttribute>>> {

    private static final String GROUPED_BY_GENDER_AGE_KEY = "grouped-by-gender-age-key";

    private final Cache<String, Object> cache;
    private final GroupingProvider<Map<String, List<PatientAttribute>>> patientGroupingProvider;
    private final String gender;
    private final String cacheKeyByGender;

    public AgeByGenderGroupingProvider(
            String gender,
            GroupingProvider<Map<String, List<PatientAttribute>>> patientGroupingProvider) {
        this.gender = gender;
        this.cacheKeyByGender = GROUPED_BY_GENDER_AGE_KEY + "-" + gender;
        this.patientGroupingProvider = patientGroupingProvider;
        this.cache = CacheProvider.getCache(EXP_MINUTES_10);
    }

    /**
     * Groups data by patient's gender and age
     *
     * @return Age data grouped by gender
     * @throws GroupingProviderException when there is an anomaly
     *                                   found during grouping operation
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<List<PatientAttribute>> getGroupedData()
            throws GroupingProviderException {

        try {
            return (List<List<PatientAttribute>>) this.cache.get(cacheKeyByGender,
                    (Callable<List<List<PatientAttribute>>>) () -> patientGroupingProvider.getGroupedData()
                            .values()
                            .stream()
                            .map(pl -> pl.stream() // filter Gender and Age attributes
                                    .filter(a -> a.isGender(gender)
                                            || a.isAge())
                                    .filter(distinctByKey(PatientAttribute::getAttributeName)) // remove duplicates
                                    .collect(Collectors.toList())
                            )
                            .filter(pl -> pl.size() > 1) // filter out non matching gender data
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            throw new GroupingProviderException(e);
        }

    }
}
