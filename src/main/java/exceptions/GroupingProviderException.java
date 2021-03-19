package exceptions;

/**
 * This checked exception will be thrown if there is
 * an anomaly found grouping the data.
 *
 * @version 1.0
 * @since 1.0
 * Date: 27-feb-2021
 */
public class GroupingProviderException
        extends Exception {

    public GroupingProviderException() { super(); }

    public GroupingProviderException(String message) {
        super(message);
    }

    public GroupingProviderException(Throwable t) {
        super(t);
    }

}
