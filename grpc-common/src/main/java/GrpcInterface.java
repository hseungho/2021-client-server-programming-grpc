import java.io.IOException;

public interface GrpcInterface {
    ClientServer.StudentList getAllStudentList();
    ClientServer.CourseList getAllCourseList();
    ClientServer.Status addStudent(ClientServer.Student student) throws IOException;
    ClientServer.StudentIdList getStudentIdList();
    ClientServer.Status deleteStudent(ClientServer.Id studentId) throws IOException;
}
