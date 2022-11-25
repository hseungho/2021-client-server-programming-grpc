import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataGrpcTest {

    private final DataGrpc dataGrpc = DataGrpc.getInstance();

    @Test
    @DisplayName("모든 학생 데이터 조회")
    void getAllStudentList() {
        ClientServer.StudentList studentList = dataGrpc.getAllStudentList();
        System.out.println(studentList);
    }

    @Test
    @DisplayName("모든 강의 데이터 조회")
    void getAllCourseList() {
        ClientServer.CourseList courseList = dataGrpc.getAllCourseList();
        System.out.println(courseList);
    }
}