package persistence.stores;

import cache.CacheProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.models.PatientAttribute;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PatientAttributeDataStoreTest {

    private DataStore<List<List<String>>, List<PatientAttribute>> dataStore;
    
    @Before
    public void setUp() {
        this.dataStore = new PatientAttributeDataStore();
        CacheProvider.clearAllCache();
    }

    @After
    public void tearDown() {
        this.dataStore = null;
    }

    @Test(expected = NullPointerException.class)
    public void testEmptyData()
            throws Exception {

        this.dataStore.getData(null);
    }

    @Test
    public void testEmptyCsvData()
            throws Exception {
        List<PatientAttribute> patientAttributes =
                this.dataStore.getData(Collections.emptyList());

        assertTrue("Data is not empty",
                    patientAttributes.isEmpty());
    }

    @Test
    public void testPatientData()
            throws Exception {

        final List<List<String>> csvData = mockCsvData();
        List<PatientAttribute> patientAttributes = this.dataStore.getData(csvData);

        assertFalse("Data is empty",
                    patientAttributes.isEmpty());

        final PatientAttribute age = patientAttributes.get(0);

        assertNotEquals("Invalid Attribute name",
                    "Gender",
                            age.getAttributeName());

        assertEquals("Invalid Attribute name",
                    "Age",
                            age.getAttributeName());

        assertEquals("Invalid Attribute value",
                "23",
                        age.getAttributeValue());
    }

    @Test
    public void testPatientAttributeGetSet() {
        final PatientAttribute mock = new PatientAttribute();
        mock.setPatientId("10");
        mock.setDate(LocalDate.MIN);
        mock.setAttributeName("BMI");
        mock.setAttributeValue("65");

        assertEquals("Invalid patient id",
                "10",
                        mock.getPatientId());

        assertEquals("Invalid date",
                        LocalDate.MIN,
                        mock.getDate());

        assertEquals("Invalid patient attribute",
                "BMI",
                        mock.getAttributeName());

        assertEquals("Invalid patient attribute value",
                "65",
                        mock.getAttributeValue());
    }

    @Test
    public void testPatientAttributeBuilder() {
        final PatientAttribute mock = PatientAttribute.builder()
                .date(LocalDate.MIN)
                .patientId("10")
                .attributeName("BMI")
                .attributeValue("70")
                .build();

        assertEquals("Invalid patient id",
                "10",
                mock.getPatientId());

        assertEquals("Invalid date",
                LocalDate.MIN,
                mock.getDate());

        assertEquals("Invalid patient attribute",
                "BMI",
                mock.getAttributeName());

        assertEquals("Invalid patient attribute value",
                "70",
                mock.getAttributeValue());
    }

    private List<List<String>> mockCsvData() {
        return Arrays.asList(
                Arrays.asList("2019-12-09", "1", "Age", "23"),
                Arrays.asList("2019-12-09", "1", "Blood pressure", "160"),
                Arrays.asList("2019-12-09", "1", "Gender", "F"),
                Arrays.asList("2019-12-09", "1", "Glucose", "11.1"),
                Arrays.asList("2019-12-09", "1", "Diabetes", "TRUE"),
                Arrays.asList("2019-12-09", "1", "WCC", "120")
        );
    }
}