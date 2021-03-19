package pks;

import io.CsvReaderImpl;
import io.DelimitedReader;
import persistence.models.PatientAttribute;
import persistence.stores.CsvDataStore;
import persistence.stores.DataStore;
import persistence.stores.PatientAttributeDataStore;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {

	public static void main(String[] args) {

		try {

			final PatientData patientData = new PatientDataImpl(
					createCsvDataStore(),
					createPatientDataStore()
			);

			System.out.println("Number of patients: " + patientData.getNumberOfPatients());
			System.out.println("Number of episodes: " + patientData.getNumberOfEpisodes());

			System.out.println("Average age male: " + patientData.getAverageAgeByGender("M") );
			System.out.println("Average age male: " + patientData.getAverageAgeByGender("F"));

		} catch (URISyntaxException e) {
			throw new RuntimeException("Invalid CSV file");
		}

	}


	private static DataStore<Boolean, List<List<String>>> createCsvDataStore()
			throws URISyntaxException {

		final URI csvFileUri = ClassLoader.getSystemResource("messages.csv")
				.toURI();
		final Path csvFilePath = Paths.get(csvFileUri);

		final DelimitedReader<Path, List<List<String>>> csvReader = new CsvReaderImpl();

		return new CsvDataStore(csvReader, csvFilePath);
	}

	private static DataStore<List<List<String>>, List<PatientAttribute>> createPatientDataStore() {
		return new PatientAttributeDataStore();
	}

}
