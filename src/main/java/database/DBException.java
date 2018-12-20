package database;

public class DBException extends Exception {
    public DBException(String message) {
        super(message);
    }

    public DBException(Throwable throwable) {
        super(throwable);
    }

    public DBException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
