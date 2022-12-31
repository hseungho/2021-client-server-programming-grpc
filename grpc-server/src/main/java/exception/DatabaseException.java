package exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String errorMessage) {
        super(errorMessage);
    }

    public static class NotExistException extends DatabaseException {
        public NotExistException(String errorMessage) {
            super(errorMessage);
        }
    }
}
