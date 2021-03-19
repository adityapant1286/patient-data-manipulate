package exceptions;

/**
 * This runtime exception can be used to wrap the exceptions
 * occurred in the service layer and display human readable messages.
 *
 * @author Aditya Pant
 * @version 1.0
 * @since 1.0
 * Date: 27-feb-2021
 */
public class PatientDataException
        extends RuntimeException {

    public PatientDataException() {
        super();
    }

    public PatientDataException(String message) {
        super(message);
    }

    public PatientDataException(Throwable t) {
        super(t);
    }
}
