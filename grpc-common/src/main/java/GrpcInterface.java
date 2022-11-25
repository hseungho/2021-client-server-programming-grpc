public interface GrpcInterface {
    ClientServer.StudentList getAllStudentList();
    ClientServer.CourseList getAllCourseList();
    ClientServer.Status addStudent(ClientServer.Student student);
    ClientServer.StudentIdList getStudentIdList();
}
