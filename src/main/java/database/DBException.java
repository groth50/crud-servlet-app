package database;

/**
 * Defines a exception a {@link DBService}
 * can throw if the query for database fails
 *
 * @autor Alex
 */
public class DBException extends Exception {

    /**
     * The throwable that caused this throwable to get thrown, or null if this
     * throwable was not caused by another throwable.
     */
    private Throwable rootCause;

    /**
     * Constructs a new database exception with the
     * specified message. The message can be written
     * to the server log and/or displayed for the user.
     *
     * @param message a String specifying the text of
     *				  the exception message
     *
     */
    public DBException(String message) {
        super(message);
    }

    /**
     * Constructs a new database exception when the {@link DBService}
     * needs to throw an the "root cause" exception that interfered with its
     * normal operation.
     *
     * @param rootCause	the Throwable exception	that
     *                  interfered with the DBService's
     *				    normal operation, making this
     *			        database exception necessary
     *
     */
    public DBException(Throwable rootCause) {
        super(rootCause);
        this.rootCause = rootCause;
    }

    /**
     * Constructs a new database exception when the {@link DBService}
     * needs to throw an exception and include a message
     * about the "root cause" exception that interfered with its
     * normal operation, including a description message.
     *
     *
     * @param message a String containing the text
     *                of the exception message
     * @param rootCause	the Throwable exception	that
     *                  interfered with the DBService's
     *				    normal operation, making this
     *			        database exception necessary
     *
     */
    public DBException(String message, Throwable rootCause) {
        super(message, rootCause);
        this.rootCause = rootCause;
    }

    /**
     * Getter for rootCause
     *
     * @return {@link Throwable}
     */
    public Throwable getRootCause() {
        return rootCause;
    }
}
