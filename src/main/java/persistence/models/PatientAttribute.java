package persistence.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 *  A model class of an each row of Csv file.
 *  The patient data is stored vertically with
 *  indefinite attributes
 *
 *  @version 1.0
 *  @since 1.0
 *  Date: 27-feb-2021
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PatientAttribute {
    LocalDate date;
    String patientId;
    String attributeName;
    String attributeValue;

    /**
     *  A shorthand method for identifying a gender attribute
     *
     * @return {@code true} if the current attribute contains gender
     *          information, {@code false} otherwise
     */
    public boolean isGender() {
        return this.attributeName.equals("Gender");
    }

    /**
     * A shorthand method for identifying the specific gender attribute
     *
     * @param gender A value to lookup
     * @return {@code true} if the current attribute contains gender
     *          information, {@code false} otherwise
     */
    public boolean isGender(String gender) {
        return isGender() && this.attributeValue.equals(gender);
    }

    /**
     * A shorthand method for identifying an age attribute
     *
     * @return {@code true} if the current attribute contains age
     *          information, {@code false} otherwise
     */
    public boolean isAge() {
        return this.attributeName.equals("Age");
    }

    /**
     * A shorthand method for identifying glucose attribute
     *
     * @return {@code true} if the current attribute contains glucose
     *          information, {@code false} otherwise
     */
    public boolean isGlucose() { return this.attributeName.equals("Glucose"); }

    /**
     * A shorthand method for identifying blood pressure attribute
     *
     * @return {@code true} if the current attribute contains blood pressure
     *          information, {@code false} otherwise
     */
    public boolean isBloodPressure() { return this.attributeName.equals("Blood pressure"); }
}
