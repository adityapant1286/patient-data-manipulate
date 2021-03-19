package pks;

public interface PatientData {

	long getNumberOfEpisodes();

	long getNumberOfPatients();

	long getNumberOfPatientsByGender(String gender);

	double getAverageAgeByGender(String gender);
}
