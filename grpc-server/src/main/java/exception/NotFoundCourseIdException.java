package exception;

public class NotFoundCourseIdException extends LMSException {
    public NotFoundCourseIdException() {
        super("could not find course id");
    }
}