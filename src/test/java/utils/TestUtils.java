package utils;

import persistence.models.PatientAttribute;
import pks.grouping.GroupingKey;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static persistence.enums.PatientCsvColumns.ATTRIBUTE_NAME;
import static persistence.enums.PatientCsvColumns.ATTRIBUTE_VALUE;
import static persistence.enums.PatientCsvColumns.DATE;
import static persistence.enums.PatientCsvColumns.PATIENT_ID;

public final class TestUtils {

    private TestUtils() {}

    public static List<PatientAttribute> validPatientAttributes() {
        final List<List<String>> csvData = Arrays.asList(
                Arrays.asList("2019-12-09", "1", "Age", "23"),
                Arrays.asList("2019-12-09", "1", "Blood pressure", "160"),
                Arrays.asList("2019-12-09", "1", "Gender", "F"),
                Arrays.asList("2019-12-09", "1", "Glucose", "11.1"),
                Arrays.asList("2019-12-09", "1", "Diabetes", "TRUE"),
                Arrays.asList("2019-12-09", "1", "WCC", "120"),
                Arrays.asList("2019-12-09", "2", "Age", "24"),
                Arrays.asList("2019-12-09", "2", "Blood pressure", "190"),
                Arrays.asList("2019-12-09", "2", "Glucose", "5.5"),
                Arrays.asList("2019-12-09", "2", "Diabetes", "FALSE"),
                Arrays.asList("2019-12-09", "2", "Gender", "M"),
                Arrays.asList("2019-12-09", "2", "WCC", "312"),
                Arrays.asList("2019-12-11", "1", "Age", "23"),
                Arrays.asList("2019-12-11", "1", "Blood pressure", "180"),
                Arrays.asList("2019-12-11", "1", "Glucose", "11.1"),
                Arrays.asList("2019-12-11", "1", "Diabetes", "TRUE"),
                Arrays.asList("2019-12-11", "1", "WCC", "212"),
                Arrays.asList("2019-12-14", "3", "Age", "26"),
                Arrays.asList("2019-12-14", "3", "Blood pressure", "210"),
                Arrays.asList("2019-12-14", "3", "Glucose", "9.7"),
                Arrays.asList("2019-12-14", "3", "Diabetes", "TRUE"),
                Arrays.asList("2019-12-14", "3", "Gender", "F"),
                Arrays.asList("2019-12-14", "3", "WCC", "110")
        );

        return csvData.stream()
                .map(l -> new PatientAttribute(
                        LocalDate.parse(l.get(DATE.ordinal()).trim()),
                        l.get(PATIENT_ID.ordinal()).trim(),
                        l.get(ATTRIBUTE_NAME.ordinal()).trim(),
                        l.get(ATTRIBUTE_VALUE.ordinal()).trim()
                ))
                .collect(Collectors.toList());
    }

    public static Map<GroupingKey, List<PatientAttribute>> groupByEpisode() {
        return validPatientAttributes().stream()
                .collect(
                        groupingBy(a -> new GroupingKey(a.getDate(), a.getPatientId()))
                );
    }

    public static Map<String, List<PatientAttribute>> groupByPatient() {
        return validPatientAttributes().stream()
                .collect(
                        groupingBy(PatientAttribute::getPatientId)
                );
    }

}
