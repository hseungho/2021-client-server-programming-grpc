package exception;

public class NotFoundCourseIdException extends LMSException {
    public NotFoundCourseIdException() {
        super("ERROR: sorry, cannot find this course id");
    }
}