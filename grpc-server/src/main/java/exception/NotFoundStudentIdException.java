package exception;

public class NotFoundStudentIdException extends LMSException {
    public NotFoundStudentIdException() {
        super("could not find student id");
    }
}
