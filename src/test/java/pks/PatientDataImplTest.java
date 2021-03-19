package pks;

import cache.CacheProvider;
import exceptions.DataStoreException;
import exceptions.PatientDataException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.models.PatientAttribute;
import persistence.stores.CsvDataStore;
import persistence.stores.DataStore;
import persistence.stores.PatientAttributeDataStore;
import pks.grouping.AgeByGenderGroupingProvider;
import pks.grouping.EpisodeGroupingProvider;
import pks.grouping.GroupingKey;
import pks.grouping.GroupingProvider;
import pks.grouping.PatientGroupingProvider;
import utils.TestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PatientDataImplTest {

    private PatientData patientData;
    private DataStore<Boolean, List<List<String>>> csvDataStore;
    private DataStore<List<List<String>>, List<PatientAttribute>> patientDataStore;

    private List<List<String>> mockCsvData;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        csvDataStore = mock(CsvDataStore.class);
        patientDataStore = mock(PatientAttributeDataStore.class);
        mockCsvData = (List<List<String>>) mock(List.class);

        patientData = new PatientDataImpl(csvDataStore, patientDataStore);
        CacheProvider.clearAllCache();
    }

    @After
    public void tearDown() {
        patientData = null;
        csvDataStore = null;
        mockCsvData = null;
        patientDataStore = null;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNumOfEpisodesZero() throws Exception {

        final GroupingProvider<Map<GroupingKey, List<PatientAttribute>>> episodeGroupingProvider =
                mock(EpisodeGroupingProvider.class);

        when(episodeGroupingProvider.getGroupedData())
                .thenReturn((Map<GroupingKey, List<PatientAttribute>>) Collections.EMPTY_MAP);

        final long numOfEpisodes = patientData.getNumberOfEpisodes();

        assertEquals("Invalid number of episodes",
                0,
                        numOfEpisodes);
    }

    @Test
    public void testNumOfEpisodesNonZero() throws Exception {

        when(csvDataStore.getData(true))
                .thenReturn(mockCsvData);

        when(patientDataStore.getData(mockCsvData))
                .thenReturn(TestUtils.validPatientAttributes());

        final long numOfEpisodes = patientData.getNumberOfEpisodes();

        assertEquals("Invalid number of episodes",
                4,
                numOfEpisodes);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNumOfPatientsZero() throws Exception {
        final GroupingProvider<Map<String, List<PatientAttribute>>> patientGroupingProvider =
                mock(PatientGroupingProvider.class);

        when(patientGroupingProvider.getGroupedData())
                .thenReturn((Map<String, List<PatientAttribute>>) Collections.EMPTY_MAP);

        final long numOfPatients = patientData.getNumberOfPatients();

        assertEquals("Invalid number of patients",
                0,
                numOfPatients);
    }

    @Test
    public void testNumOfPatientsNonZero() throws Exception {

        when(csvDataStore.getData(true))
                .thenReturn(mockCsvData);

        when(patientDataStore.getData(mockCsvData))
                .thenReturn(TestUtils.validPatientAttributes());

        final long numOfPatients = patientData.getNumberOfPatients();

        assertEquals("Invalid number of patients",
                3,
                numOfPatients);
    }

    @Test(expected = NullPointerException.class)
    public void testNumOfPatientsByGenderException() throws Exception {
        patientData.getNumberOfPatientsByGender(null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNumOfPatientsByGenderZero() throws Exception {
        final GroupingProvider<List<List<PatientAttribute>>> ageByGenderGroupingProvider =
                mock(AgeByGenderGroupingProvider.class);

        when(ageByGenderGroupingProvider.getGroupedData())
                .thenReturn((List<List<PatientAttribute>>) Collections.EMPTY_LIST);

        final long numOfPatientsM = patientData.getNumberOfPatientsByGender("M");

        assertEquals("Invalid number of patients by gender M",
                0,
                numOfPatientsM);

        final long numOfPatientsF = patientData.getNumberOfPatientsByGender("F");

        assertEquals("Invalid number of patients by gender F",
                0,
                numOfPatientsF);
    }

    @Test
    public void testNumOfPatientsByGenderNonZero() throws Exception {

        when(csvDataStore.getData(true))
                .thenReturn(mockCsvData);

        when(patientDataStore.getData(mockCsvData))
                .thenReturn(TestUtils.validPatientAttributes());

        final long numOfPatientsM = patientData.getNumberOfPatientsByGender("M");

        assertEquals("Invalid number of patients by gender M",
                1,
                numOfPatientsM);

        final long numOfPatientsF = patientData.getNumberOfPatientsByGender("F");

        assertEquals("Invalid number of patients by gender F",
                2,
                numOfPatientsF);
    }

    @Test(expected = NullPointerException.class)
    public void testAverageAgeByGenderException() throws Exception {
        patientData.getAverageAgeByGender(null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAverageAgeByGenderZero() throws Exception {
        final GroupingProvider<List<List<PatientAttribute>>> ageByGenderGroupingProvider =
                mock(AgeByGenderGroupingProvider.class);

        when(ageByGenderGroupingProvider.getGroupedData())
                .thenReturn((List<List<PatientAttribute>>) Collections.EMPTY_LIST);

        final double avgAgeByGenderM = patientData.getAverageAgeByGender("M");

        assertEquals("Invalid number of patients by gender M",
                0,
                        avgAgeByGenderM,
                    0.00);

        final double avgAgeByGenderF = patientData.getAverageAgeByGender("F");

        assertEquals("Invalid number of patients by gender F",
                0,
                        avgAgeByGenderF,
                    0.00);
    }

    @Test
    public void testAverageAgeByGenderNonZero() throws Exception {

        when(csvDataStore.getData(true))
                .thenReturn(mockCsvData);

        when(patientDataStore.getData(mockCsvData))
                .thenReturn(TestUtils.validPatientAttributes());

        final double avgAgeByGenderM = patientData.getAverageAgeByGender("M");

        assertEquals("Invalid number of patients by gender M",
                24,
                avgAgeByGenderM,
                0.01);

        final double avgAgeByGenderF = patientData.getAverageAgeByGender("F");

        assertEquals("Invalid number of patients by gender F",
                24.5,
                avgAgeByGenderF,
                0.01);
    }

    @Test(expected = PatientDataException.class)
    public void testGroupingProviderException() throws PatientDataException {
        throw new PatientDataException();
    }

    @Test(expected = PatientDataException.class)
    public void testGroupingProviderExceptionMessage() throws PatientDataException {
        throw new PatientDataException("test");
    }

    @Test(expected = PatientDataException.class)
    public void testGroupingProviderExceptionThrowable() throws PatientDataException {
        throw new PatientDataException(new Exception());
    }

    @Test
    public void myTest() throws DataStoreException {
        List<PatientAttribute> collect = TestUtils.validPatientAttributes()
                .stream()
                .filter(PatientAttribute::isGlucose)
                .collect(Collectors.toList());

        System.out.println(collect);

    }
}