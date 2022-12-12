import java.io.IOException;

public interface GrpcInterface {
    ClientServer.StudentList getAllStudentList();
    ClientServer.CourseList getAllCourseList();
    ClientServer.IdList getStudentIdList();
    ClientServer.IdList getCourseIdList();
    ClientServer.Status addStudent(ClientServer.Student student) throws IOException;
    ClientServer.Status addCourse(ClientServer.Course course) throws IOException;
    ClientServer.Status deleteStudent(ClientServer.Id studentId) throws IOException;
}
