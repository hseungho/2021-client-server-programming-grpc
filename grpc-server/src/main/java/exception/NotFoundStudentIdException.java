package exception;

public class NotFoundStudentIdException extends LMSException {
    public NotFoundStudentIdException() {
        super("ERROR: sorry, cannot find this student id");
    }
}
