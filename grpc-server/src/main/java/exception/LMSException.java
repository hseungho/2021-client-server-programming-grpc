package exception;

public class LMSException extends RuntimeException {
    public LMSException(String errorMessage) {
        super(errorMessage);
    }
}
