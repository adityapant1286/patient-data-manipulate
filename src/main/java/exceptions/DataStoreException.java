package exceptions;

/**
 * This checked exception will be thrown when there is
 * an anomaly found during the data store operation.
 *
 * @version 1.0
 * @since 1.0
 * Date: 27-feb-2021
 */
public class DataStoreException
        extends Exception {

    public DataStoreException() { super(); }

    public DataStoreException(String message) {
        super(message);
    }

    public DataStoreException(Throwable t) {
        super(t);
    }
}
