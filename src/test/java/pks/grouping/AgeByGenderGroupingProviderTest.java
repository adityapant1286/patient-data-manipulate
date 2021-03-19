package pks.grouping;

import cache.CacheProvider;
import exceptions.GroupingProviderException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.models.PatientAttribute;
import utils.TestUtils;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AgeByGenderGroupingProviderTest {

    private GroupingProvider<List<List<PatientAttribute>>> provider;
    private GroupingProvider<Map<String, List<PatientAttribute>>> patientGroupingProvider;
    private List<List<PatientAttribute>> groupedData;

    @Before
    public void setUp() {

        patientGroupingProvider = mock(PatientGroupingProvider.class);
        CacheProvider.clearAllCache();
    }

    @After
    public void tearDown() {
        provider = null;
    }

    @Test(expected = GroupingProviderException.class)
    public void testException()
            throws Exception {

        provider = new AgeByGenderGroupingProvider("M", patientGroupingProvider);

        when(patientGroupingProvider.getGroupedData())
                .thenReturn(null);

        provider.getGroupedData();
    }

    @Test
    public void testGroupedDataByGenderM()
            throws Exception {

        provider = new AgeByGenderGroupingProvider("M", patientGroupingProvider);

        when(patientGroupingProvider.getGroupedData())
                .thenReturn(TestUtils.groupByPatient());

        groupedData = provider.getGroupedData();

        assertEquals("Invalid number of patients with gender M",
                1,
                groupedData.size());

        assertEquals("Invalid patient id",
                "2",
                groupedData.get(0).get(0).getPatientId());

        assertEquals("Invalid patient age",
                "24",
                groupedData.get(0).get(0).getAttributeValue());
    }

    @Test
    public void testGroupedDataByGenderF()
            throws Exception {

        provider = new AgeByGenderGroupingProvider("F", patientGroupingProvider);

        when(patientGroupingProvider.getGroupedData())
                .thenReturn(TestUtils.groupByPatient());

        groupedData = provider.getGroupedData();

        assertEquals("Invalid number of patients with gender F",
                2,
                    groupedData.size());

        assertEquals("Invalid patient id",
                "1",
                groupedData.get(0).get(0).getPatientId());

        assertEquals("Invalid patient age",
                "23",
                groupedData.get(0).get(0).getAttributeValue());

        assertEquals("Invalid patient id",
                "3",
                groupedData.get(1).get(0).getPatientId());

        assertEquals("Invalid patient age",
                "26",
                groupedData.get(1).get(0).getAttributeValue());

    }

}