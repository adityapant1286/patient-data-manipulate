package io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static utils.Constants.EXCLUDE_CSV_HEADERS;

public class CsvReaderImplTest {

    private DelimitedReader<Path, List<List<String>>> reader = null;
    private Path csvFilePath = null;

    @Before
    public void setUp()
            throws Exception {

        final URI csvFileUri = ClassLoader.getSystemResource("messages.csv")
                .toURI();
        csvFilePath = Paths.get(csvFileUri);

        reader = new CsvReaderImpl();

    }

    @After
    public void tearDown() {
        reader = null;
    }

    @Test(expected = NullPointerException.class)
    public void testException()
            throws Exception {

        reader.read(null, false);
    }

    @Test
    public void testCsvRead()
            throws Exception {

        final List<List<String>> data = reader.read(csvFilePath, false);

        assertFalse("Csv file is empty",
                        data.isEmpty());
    }

    @Test
    public void testCsvReadWithoutHeaders()
            throws Exception {

        final List<List<String>> data = reader.read(csvFilePath, true);

        assertFalse("Csv file is empty",
                        data.isEmpty());

        assertNotEquals("Value id Date",
                    "Date",
                            data.get(0).get(0));
    }

    @Test
    public void testCsvReadWithHeaders()
            throws Exception {

        List<List<String>> data = reader.read(csvFilePath, false);

        assertFalse("Csv file is empty",
                        data.isEmpty());

        assertEquals("Date",
                        data.get(0).get(0));

        data = reader.read(csvFilePath, true);

        assertFalse("Csv file is empty",
                        data.isEmpty());

        assertNotEquals("Value id Date",
                    "Date",
                            data.get(0).get(0));
    }

    @Test
    public void testCsvConstants() {

        assertTrue("Exclude Csv header is false", EXCLUDE_CSV_HEADERS);
    }
}