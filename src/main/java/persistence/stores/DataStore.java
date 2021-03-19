package persistence.stores;

import exceptions.DataStoreException;

/**
 * A contract for storing data and return output from the storing
 * medium.
 * Candidate for functional interface.
 *
 * @param <Input>
 * @param <Output>
 * @version 1.0
 * @since 1.0
 * Date: 27-feb-2021
 */
public interface DataStore<Input, Output> {

    /**
     * Store data into a medium and returns output to the
     * caller.
     * <br>
     * <b>Example:</b> Storing data into Cache or in memory map
     *
     * @param input Any input type which is used by the storing medium
     * @return Data from store
     * @throws DataStoreException The caller needs to handle in case of anomaly
     *                            in the storing medium
     */
    Output getData(Input input) throws DataStoreException;
}
