package exception;

public class NotFoundCourseIdException extends MyException {
    public NotFoundCourseIdException() {
        super("ERROR: sorry, cannot find this course id");
    }
}