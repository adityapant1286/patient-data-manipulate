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

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PatientGroupingProviderTest {

    private GroupingProvider<Map<String, List<PatientAttribute>>> provider;
    private DataStore<Boolean, List<List<String>>> csvDataStore;
    private DataStore<List<List<String>>, List<PatientAttribute>> patientDataStore;

    private List<List<String>> mockCsvData;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        csvDataStore = mock(CsvDataStore.class);
        patientDataStore = mock(PatientAttributeDataStore.class);

        mockCsvData = (List<List<String>>) mock(List.class);

        provider = new PatientGroupingProvider(csvDataStore, patientDataStore);
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

        final Map<String, List<PatientAttribute>> groupedData = provider.getGroupedData();

        assertEquals("Invalid number of patients",
                3,
                groupedData.size());
    }

    @Test
    public void testPatientAttributes()
            throws Exception {

        when(csvDataStore.getData(true))
                .thenReturn(mockCsvData);

        when(patientDataStore.getData(mockCsvData))
                .thenReturn(TestUtils.validPatientAttributes());

        final Map<String, List<PatientAttribute>> groupedData = provider.getGroupedData();

        assertEquals("Invalid number of patients",
                3,
                groupedData.size());

        assertEquals("Invalid number of attributes of patient id 1",
                11,
                groupedData.get("1").size());

        assertEquals("Invalid number of attributes of patient id 2",
                6,
                groupedData.get("2").size());

        assertEquals("Invalid number of attributes of patient id 3",
                6,
                groupedData.get("3").size());

    }


}