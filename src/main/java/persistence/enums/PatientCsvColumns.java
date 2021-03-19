package persistence.enums;

/**
 *  The column of Patient data Csv file.
 *  The order of enum properties must be as per the
 *  order of columns in a Csv file.
 *  <br>
 *  <b>Important:</b> Changes in Csv columns will require changes
 *  in the order of enum properties.
 *
 *  @version 1.0
 *  @since 1.0
 *  Date: 27-feb-2021
 */
public enum PatientCsvColumns {
    DATE, PATIENT_ID, ATTRIBUTE_NAME, ATTRIBUTE_VALUE
}
