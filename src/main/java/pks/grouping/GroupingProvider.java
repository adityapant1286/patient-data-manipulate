package pks.grouping;

import exceptions.GroupingProviderException;

/**
 * A contract for grouping data provider.
 *  Candidate for functional interface.
 *
 * @param <Output>
 * @version 1.0
 * @since 1.0
 * Date: 27-feb-2021
 */
public interface GroupingProvider<Output> {

    /**
     * Provides a contract for grouping data.
     * @return Grouped data
     * @throws GroupingProviderException when there is an anomaly
     *                                   found during grouping operation
     */
    Output getGroupedData() throws GroupingProviderException;
}
