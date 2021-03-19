package persistence.stores;

import cache.CacheProvider;
import com.google.common.cache.Cache;
import exceptions.DataStoreException;
import persistence.models.PatientAttribute;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static persistence.enums.PatientCsvColumns.ATTRIBUTE_NAME;
import static persistence.enums.PatientCsvColumns.ATTRIBUTE_VALUE;
import static persistence.enums.PatientCsvColumns.DATE;
import static persistence.enums.PatientCsvColumns.PATIENT_ID;
import static utils.Constants.EXP_MINUTES_125;

/**
 * An implementation of patient data store.
 * The Csv data transformed in to a list of {@link PatientAttribute}
 * and stored in a cache for fast retrieval.
 * <br>
 * <b>Note:</b> Default cache expiration 125 minutes
 * <br><br>
 * This implementation uses google guava library for caching.
 * <br>
 * <a href="https://github.com/google/guava/wiki/CachesExplained">CachesExplained</a>
 *
 *
 * @version 1.0
 * @since 1.0
 * Date: 27-feb-2021
 * @see Cache
 */
public class PatientAttributeDataStore
        implements DataStore<List<List<String>>, List<PatientAttribute>> {

    private final Cache<String, Object> cache;

    private static final String GROUP_BY_PATIENT_ID = "group-by-patient-id-key";

    public PatientAttributeDataStore() {
        this.cache = CacheProvider.getCache(EXP_MINUTES_125);
    }

    /**
     * Transforms Csv data to a list of {@link PatientAttribute} using the index of
     * the columns.
     * The data stored into a cache for fast retrieval,
     * if not exists already.
     *
     * @param csvData Rows and columns from Csv file
     * @return A list of {@link PatientAttribute}
     * @throws DataStoreException Any exception thrown by caching provider
     *                            will be wrapped in this exception.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PatientAttribute> getData(List<List<String>> csvData)
            throws DataStoreException {

        Objects.requireNonNull(csvData, "Invalid CSV Data");

        try {
            return (List<PatientAttribute>) cache.get(
                    GROUP_BY_PATIENT_ID,
                    (Callable<List<PatientAttribute>>) () ->
                            csvData.stream() // a list of columns data mapped to model object
                                    .map(e -> new PatientAttribute(
                                                    LocalDate.parse(e.get(DATE.ordinal()).trim()),
                                                    e.get(PATIENT_ID.ordinal()).trim(),
                                                    e.get(ATTRIBUTE_NAME.ordinal()).trim(),
                                                    e.get(ATTRIBUTE_VALUE.ordinal()).trim()
                                            )
                                    )
                                    .collect(Collectors.toList())
            );
        } catch (ExecutionException e) {
            throw new DataStoreException(e);
        }

    }

}
