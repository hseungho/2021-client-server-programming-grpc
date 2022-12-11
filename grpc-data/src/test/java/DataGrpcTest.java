import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataGrpcTest {

    private final DataGrpc dataGrpc = DataGrpc.getInstance();

    @Test
    @DisplayName("Get all students list")
    void getAllStudentList() {
        ClientServer.StudentList studentList = dataGrpc.getAllStudentList();
        System.out.println(studentList);
    }

    @Test
    @DisplayName("get all courses list")
    void getAllCourseList() {
        ClientServer.CourseList courseList = dataGrpc.getAllCourseList();
        System.out.println(courseList);
    }
}