package pks;

import exceptions.GroupingProviderException;
import exceptions.PatientDataException;
import functional.DistinctPredicate;
import persistence.models.PatientAttribute;
import persistence.stores.DataStore;
import pks.grouping.AgeByGenderGroupingProvider;
import pks.grouping.EpisodeGroupingProvider;
import pks.grouping.GroupingKey;
import pks.grouping.GroupingProvider;
import pks.grouping.PatientGroupingProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *  Implementation of multiple operations of {@link PatientData} contract
 *
 * @version 1.0
 * @since 1.0
 * Date: 28-feb-2021
 */
public class PatientDataImpl
        implements PatientData {

    private final DataStore<Boolean, List<List<String>>> csvDataStore;
    private final DataStore<List<List<String>>, List<PatientAttribute>> patientDataStore;

    public PatientDataImpl(DataStore<Boolean, List<List<String>>> csvDataStore,
                           DataStore<List<List<String>>, List<PatientAttribute>> patientDataStore) {
        this.csvDataStore = csvDataStore;
        this.patientDataStore = patientDataStore;
    }

    /**
     * Retrieves data from providers and computes the number of episodes
     * <br>
     * An episode is a group of all attributes for one patient on a single day
     *
     * @return number of episodes
     */
    @Override
    public long getNumberOfEpisodes() {
        long numOfEpisodes;
        GroupingProvider<Map<GroupingKey, List<PatientAttribute>>> provider;
        try {

            provider = new EpisodeGroupingProvider(csvDataStore, patientDataStore);

            numOfEpisodes = provider.getGroupedData()
                                    .size();

        } catch (GroupingProviderException e) {
            // log exception to logging systems
            throw new PatientDataException("Error in data retrieval");
        } finally {
            provider = null;
        }
        return numOfEpisodes;
    }

    /**
     * Retrieves data from providers and computes the number of patients
     *
     * @return number of patients
     */
    @Override
    public long getNumberOfPatients() {
        long numOfPatients;
        GroupingProvider<Map<String, List<PatientAttribute>>> provider;
        try {
            provider = new PatientGroupingProvider(csvDataStore, patientDataStore);

            numOfPatients = provider.getGroupedData()
                                    .size();

        } catch (GroupingProviderException e) {
            // log exception to logging systems
            throw new PatientDataException("Error in data retrieval");
        } finally {
            provider = null;
        }

        return numOfPatients;
    }

    /**
     * Retrieves data from providers by gender and computes the number of patients
     * by gender.
     *
     * @param gender the gender value to compute the number of patients by gender
     * @return number of patients by gender
     */
    @Override
    public long getNumberOfPatientsByGender(String gender) {
        Objects.requireNonNull(gender, "Invalid gender value");

        long numOfPatients;

        GroupingProvider<List<List<PatientAttribute>>> provider;

        try {
            provider = new AgeByGenderGroupingProvider(
                            gender,
                            new PatientGroupingProvider(csvDataStore, patientDataStore)
                        );

            numOfPatients = provider.getGroupedData()
                                    .size();

        } catch (GroupingProviderException e) {
            // log exception to logging systems
            throw new PatientDataException("Error in data retrieval");
        } finally {
            provider = null;
        }

        return numOfPatients;
    }

    /**
     * Retrieves data from providers by gender and computes the average age of patients
     * by gender.

     * @param gender the gender value to compute the number of patients by gender
     * @return average age by gender
     */
    @Override
    public double getAverageAgeByGender(String gender) {
        Objects.requireNonNull(gender, "Invalid gender value");

        double averageAgeByGender = 0;
        GroupingProvider<List<List<PatientAttribute>>> provider;

        try {

            provider = new AgeByGenderGroupingProvider(
                    gender,
                    new PatientGroupingProvider(csvDataStore, patientDataStore)
            );
            System.out.println(provider.getGroupedData());
            List<List<Double>> collect = provider.getGroupedData()
                    .stream()
                    .map(l -> l.stream() //
                            .filter(PatientAttribute::isAge)
                            .map(a -> Double.valueOf(a.getAttributeValue()))
                            .collect(Collectors.toList())
                    )
                    .collect(Collectors.toList());
//                                        .average()
//                                        .orElse(0);
            System.out.println(collect);

        } catch (GroupingProviderException e) {
            // log exception to logging systems
            throw new PatientDataException("Error in data retrieval");
        } finally {
            provider = null;
        }
        return averageAgeByGender;
    }

}
