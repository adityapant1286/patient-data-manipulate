package pks.grouping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * This composite key is useful when grouping
 * a stream of data based on the key properties.
 * The result is a stream of data grouped by this key.
 *
 *  @version 1.0
 *  @since 1.0
 *  Date: 28-feb-2021
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupingKey {
    LocalDate date;
    String patientId;
}
