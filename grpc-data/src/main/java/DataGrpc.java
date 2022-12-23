import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class DataGrpc implements GrpcInterface {

    public static DataGrpc instance;
    public static DataGrpc getInstance() {
        return instance != null ? instance : new DataGrpc();
    }

    private Server server;
    private static StudentList studentList;
    private static CourseList courseList;

    public DataGrpc() {
        try {
            studentList = new StudentList("Students.txt");
            courseList = new CourseList("Courses.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        System.out.println("<<<<< DATA SERVER START >>>>>");
        startServer();
    }

    @Override
    public ClientServer.StudentList getAllStudentList() {
        System.out.println("CALLED METHOD: getAllStudentList");
        ClientServer.StudentList studentListResponse;
        try {
            List<ClientServer.Student> responseList = new ArrayList<>();
            for(Student student : studentList.getAllStudentRecords()) {
                ClientServer.Student responseStudent = ClientServer.Student.newBuilder()
//                        .setId(student.getStudentId())
////                        .setName(student.getName())
//                        .setDepartment(student.getDepartment())
//                        .addAllCompletedCourseList(new ArrayList<>(student.getCompletedCourseList()))
                        .build();
                responseList.add(responseStudent);
            }
            studentListResponse = ClientServer.StudentList.newBuilder()
                    .setStatus(StringConstant.STATUS_SUCCESS)
                    .addAllStudent(responseList)
                    .build();
        } catch (MyException.NullDataException e) {
            studentListResponse = ClientServer.StudentList.newBuilder()
                    .setStatus(StringConstant.STATUS_FAILED_NO_DATA)
                    .build();
        }
        return studentListResponse;
    }

    @Override
    public ClientServer.CourseList getAllCourseList() {
        System.out.println("CALLED METHOD: getAllCourseList");
        ClientServer.CourseList courseListResponse;
        try {
            List<ClientServer.Course> responseList = new ArrayList<>();
            for(Course course : courseList.getAllCourseRecords()) {
                ClientServer.Course responseCourse = ClientServer.Course.newBuilder()
//                        .setId(course.getCourseId())
//                        .setProfName(course.getProfName())
//                        .setCourseName(course.getCourseName())
//                        .addAllPrerequisite(new ArrayList<>(course.getPrerequisite()))
                        .build();
                responseList.add(responseCourse);
            }
            courseListResponse = ClientServer.CourseList.newBuilder()
                    .setStatus(StringConstant.STATUS_SUCCESS)
                    .addAllCourse(responseList).build();
        } catch (MyException.NullDataException e) {
            courseListResponse = ClientServer.CourseList.newBuilder().setStatus(StringConstant.STATUS_FAILED_NO_DATA).build();
        }
        return  courseListResponse;
    }

    @Override
    public ClientServer.IdList getStudentIdList() {
        System.out.println("CALLED METHOD: getStudentIdList");
        List<String> studentIdList = studentList.getAllStudentId();
        return ClientServer.IdList.newBuilder().addAllId(studentIdList).build();
    }

    @Override
    public ClientServer.IdList getCourseIdList() {
        System.out.println("CALLED METHOD: getCourseIdList");
        List<String> courseIds = courseList.getAllCourseId();
        return ClientServer.IdList.newBuilder().addAllId(courseIds).build();
    }

    @Override
    public ClientServer.Status addStudent(ClientServer.Student student) throws IOException {
        System.out.println("CALLED METHOD: addStudent");
        return studentList.addStudentRecord(student) ?
                ClientServer.Status.newBuilder().setStatus(201).build() : ClientServer.Status.newBuilder().setStatus(409).build();
    }

    @Override
    public ClientServer.Status addCourse(ClientServer.Course course) throws IOException {
        System.out.println("CALLED METHOD: addCourse");
        return courseList.addCourseRecord(course) ?
                ClientServer.Status.newBuilder().setStatus(201).build() : ClientServer.Status.newBuilder().setStatus(409).build();
    }

    @Override
    public ClientServer.Status deleteStudent(ClientServer.Id studentId) throws IOException {
        System.out.println("CALLED METHOD: deleteStudent");
        return studentList.deleteStudentRecord(studentId.getId()) ?
                ClientServer.Status.newBuilder().setStatus(201).build() : ClientServer.Status.newBuilder().setStatus(409).build();
    }

    @Override
    public ClientServer.Status deleteCourse(ClientServer.Id courseId) throws IOException {
        System.out.println("CALLED METHOD: deleteCourse");
        return courseList.deleteCourseRecord(courseId.getId()) ?
                ClientServer.Status.newBuilder().setStatus(201).build() : ClientServer.Status.newBuilder().setStatus(409).build();
    }

    private void startServer() {
        Thread serverThread = new Thread(() -> {
            int port = 9090;
            server = NettyServerBuilder
                    .forAddress(new InetSocketAddress("localhost", port))
                    .addService(new DataClientServerImpl())
                    .build();

            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.err.println("\nData Server: gRPC 서버를 종료합니다.");
                System.err.println("Data Server: 서버를 종료합니다.");
                System.err.println(">>> 프로그램이 종료됩니다.");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!server.isTerminated()) {
                    server.shutdown();
                }
            }));

            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }
}
