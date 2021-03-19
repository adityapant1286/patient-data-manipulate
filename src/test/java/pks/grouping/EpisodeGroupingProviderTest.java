package pks.grouping;

import cache.CacheProvider;
import exceptions.GroupingProviderException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.models.PatientAttribute;
import persistence.stores.CsvDataStore;
import persistence.stores.DataStore;
import persistence.stores.PatientAttributeDataStore;
import utils.TestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EpisodeGroupingProviderTest {

    private GroupingProvider<Map<GroupingKey, List<PatientAttribute>>> provider;
    private DataStore<Boolean, List<List<String>>> csvDataStore;
    private DataStore<List<List<String>>, List<PatientAttribute>> patientDataStore;

    private List<List<String>> mockCsvData;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        csvDataStore = mock(CsvDataStore.class);
        patientDataStore = mock(PatientAttributeDataStore.class);

        mockCsvData = (List<List<String>>) mock(List.class);

        provider = new EpisodeGroupingProvider(csvDataStore, patientDataStore);
        CacheProvider.clearAllCache();
    }

    @After
    public void tearDown() {
        csvDataStore = null;
        patientDataStore = null;
        mockCsvData = null;
        provider = null;
    }

    @Test(expected = GroupingProviderException.class)
    public void testException()
            throws Exception {

        when(csvDataStore.getData(true))
                .thenReturn(mockCsvData);

        when(patientDataStore.getData(mockCsvData))
                .thenReturn(null);

        provider.getGroupedData();
    }

    @Test
    public void testGroupedData()
            throws Exception {

        when(csvDataStore.getData(true))
                .thenReturn(mockCsvData);

        when(patientDataStore.getData(mockCsvData))
                .thenReturn(TestUtils.validPatientAttributes());

        final Map<GroupingKey, List<PatientAttribute>> groupedData = provider.getGroupedData();

        assertEquals("Invalid number of episodes",
                4,
                        groupedData.size());
    }

    @Test
    public void testEpisodeAttributes()
            throws Exception {

        when(csvDataStore.getData(true))
                .thenReturn(mockCsvData);

        when(patientDataStore.getData(mockCsvData))
                .thenReturn(TestUtils.validPatientAttributes());

        final Map<GroupingKey, List<PatientAttribute>> groupedData = provider.getGroupedData();

        assertEquals("Invalid number of episodes",
                4,
                        groupedData.size());

        final GroupingKey key_Dec_09 = new GroupingKey(LocalDate.parse("2019-12-09"),
                                                "1");
        assertEquals("Invalid number of attributes on Dec-09",
                6,
                        groupedData.get(key_Dec_09).size());

        final GroupingKey key_Dec_11 = new GroupingKey(LocalDate.parse("2019-12-11"),
                                                "1");
        assertEquals("Invalid number of attributes on Dec-11",
                5,
                        groupedData.get(key_Dec_11).size());

    }

    @Test(expected = GroupingProviderException.class)
    public void testGroupingProviderException()
            throws GroupingProviderException {

        throw new GroupingProviderException();
    }

    @Test(expected = GroupingProviderException.class)
    public void testGroupingProviderExceptionMessage()
            throws GroupingProviderException {

        throw new GroupingProviderException("test");
    }

    @Test(expected = GroupingProviderException.class)
    public void testGroupingProviderExceptionThrowable()
            throws GroupingProviderException {

        throw new GroupingProviderException(new Exception());
    }

    @Test
    public void testGroupingKey() {
        final GroupingKey key = new GroupingKey();
        assertNotNull("Key is null", key);
    }

    @Test
    public void testGroupingKeyBuilder() {
        final GroupingKey key = GroupingKey.builder()
                                .date(LocalDate.MIN)
                                .patientId("10")
                                .build();
        assertEquals("Invalid patient id",
                "10",
                key.getPatientId());

        assertEquals("Invalid date",
                LocalDate.MIN,
                key.getDate());

    }
}