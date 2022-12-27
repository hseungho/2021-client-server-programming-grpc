package exception;

public class NotFoundStudentIdException extends MyException {
    public NotFoundStudentIdException() {
        super("ERROR: sorry, cannot find this student id");
    }
}
