import com.google.protobuf.Empty;
import dto.StudentDto;
import exception.MyException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import util.CommandLineTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ClientGrpc {

    public static ClientGrpc instance;
    public static ClientGrpc getInstance() {
        return instance != null ? instance : new ClientGrpc();
    }

    private BufferedReader br;
    private ManagedChannel channel;
    private ClientServerProtoGrpc.ClientServerProtoBlockingStub stub;
    private boolean isRunning;
    private CommandLineTable studentTable;
    private CommandLineTable courseTable;

    public ClientGrpc() {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
    }

    private void init() {
        initChannel();
        initStub();
        initTable();
    }

    private void initChannel() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();
    }

    private void initStub() {
        stub = ClientServerProtoGrpc.newBlockingStub(channel);
    }

    private void initTable() {
        studentTable = new CommandLineTable();

    }

    public void start() throws IOException {
        isRunning = true;
        selectMenu();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("******************** MENU *********************");
        System.out.println("1. List Students");
        System.out.println("2. List Courses");
        System.out.println("3. Add Student");
        System.out.println("4. Delete Student");
        System.out.println("5. Add Course");
        System.out.println("6. Delete Course");
        System.out.println("7. Make Reservation");
        System.out.println("8. List Reservation");
        System.out.println("x. Exit");
        System.out.print("메뉴 입력: ");
    }

    private void selectMenu() throws IOException {
        while(isRunning) {
            this.printMenu();
            try {
                String s = br.readLine().toLowerCase();
                switch (s) {
                    case "1" -> showStudentList();
                    case "2" -> showCourseList();
                    case "3" -> addStudent();
                    case "4" -> deleteStudent();
                    case "5" -> addCourse();
                    case "6" -> deleteCourse();
                    case "7" -> registerCourse();
                    case "8" -> showRegister();
                    case "x" -> isRunning = false;
                    default -> System.err.println("Incorrect Menu !!");
                }
            } catch (MyException e) {
                System.err.println(e.getMessage());
            } catch (StatusRuntimeException e) {
                System.err.println("sorry, we cannot service that request.");
            }
        }
    }

    private void showStudentList() {
        ClientServer.StudentList studentList = stub.getAllStudentData(Empty.newBuilder().build());
        switch (studentList.getStatus()) {
            case StringConstant.STATUS_SUCCESS -> printStudent(studentList.getStudentList());
            case StringConstant.STATUS_FAILED_NO_DATA -> System.out.println("--- NO STUDENT DATA ---");
            default -> {}
        }
    }

    private void printStudent(List<ClientServer.Student> studentList) {
        System.out.println("<<<<<<<<<<<<<<   Student List   >>>>>>>>>>>>>>");
        List<StudentDto> studentDtos = studentList.stream().map(student -> new StudentDto(
                student.getId(), student.getStudentId(), student.getFirstName(), student.getLastName(), student.getLastName(),
                student.getCompletedCourseListList().stream().map(ClientServer.Course::getCourseId).toList())).toList();


    }

    private void showCourseList() {
        ClientServer.CourseList courseList = stub.getAllCourseData(Empty.newBuilder().build());
        switch (courseList.getStatus()) {
            case StringConstant.STATUS_SUCCESS -> printCourse(courseList.getCourseList());
            case StringConstant.STATUS_FAILED_NO_DATA -> System.out.println("--- No COURSE DATA ---");
            default -> {}
        }
    }

    private void printCourse(List<ClientServer.Course> courseList) {
        System.out.println("<<<<<<<<<<<<<<   Course List   >>>>>>>>>>>>>>");
        // TODO
    }

    private void addStudent() throws IOException, MyException {
        System.out.println("<<<<<<<<<<<<<<   Add Student   >>>>>>>>>>>>>>");
        System.out.println("------Student Information------");
        System.out.print("Student ID: "); String studentId = br.readLine().trim();
        System.out.print("Student First Name: "); String studentFirstName = br.readLine().trim();
        System.out.print("Student Last Name: "); String studentLastName = br.readLine().trim();
        System.out.print("Student Department: "); String studentDept = br.readLine().trim();
        System.out.print("Student Completed Course List: "); String completedCourse = br.readLine().trim();
        if(studentId.isBlank() || studentFirstName.isBlank() || studentLastName.isBlank() || studentDept.isBlank()) {
            throw new MyException.NullDataException("You have to input every student information.");
        }
        List<String> strCompletedCourseList = List.of(completedCourse.split(" "));
        List<ClientServer.Course> completedCourseList = strCompletedCourseList.stream().map(courseId ->
                ClientServer.Course.newBuilder().setCourseId(courseId).build()).toList();

        ClientServer.Student student = ClientServer.Student.newBuilder()
                .setStudentId(studentId)
                .setFirstName(studentFirstName)
                .setLastName(studentLastName)
                .setDepartment(studentDept)
                .addAllCompletedCourseList(completedCourseList)
                .build();
        ClientServer.Status status = stub.addStudent(student);
        responseStatus(status, "ADD SUCCESS !!!");
    }

    private void deleteStudent() throws IOException, MyException {
        System.out.println("<<<<<<<<<<<<<<   Delete Student   >>>>>>>>>>>>>>");
        System.out.print("Student ID: "); String studentId = br.readLine().trim();
        if(studentId.isBlank()) {
            throw new MyException.NullDataException("You have to input student id deleted.");
        }
        ClientServer.Id requestId = ClientServer.Id.newBuilder().setId(studentId).build();
        ClientServer.Status responseStatus = stub.deleteStudent(requestId);
        responseStatus(responseStatus, "DELETE SUCCESS !!!");
    }

    private void addCourse() throws IOException, MyException {
        System.out.println("<<<<<<<<<<<<<<   Add Course   >>>>>>>>>>>>>>");
        System.out.println("------Course Information------");
        System.out.print("Course ID: "); String courseId = br.readLine().trim();
        System.out.print("Prof Name: "); String profName = br.readLine().trim();
        System.out.print("Course Name: "); String courseName = br.readLine().trim();
        System.out.print("Course Prerequisite List: "); String prerequisite = br.readLine().trim();
        if(courseId.isBlank() || profName.isBlank() || courseName.isBlank()) {
            throw new MyException.NullDataException("You have to input every course information.");
        }
        List<String> strPrerequisiteList = List.of(prerequisite.split(" "));
        List<ClientServer.Course> prerequisiteList = strPrerequisiteList.stream().map(preCourseId ->
                ClientServer.Course.newBuilder().setCourseId(preCourseId).build()).toList();

        ClientServer.Course course = ClientServer.Course.newBuilder()
                .setCourseId(courseId)
                .setProfName(profName)
                .setCourseName(courseName)
                .addAllPrerequisite(prerequisiteList).build();
        ClientServer.Status status = stub.addCourse(course);
        responseStatus(status, "ADD SUCCESS !!!");
    }

    private void deleteCourse() throws IOException, MyException {
        System.out.println("<<<<<<<<<<<<<<   Delete Course   >>>>>>>>>>>>>>");
        System.out.print("Course ID: "); String courseId = br.readLine().trim();
        if(courseId.isBlank()) {
            throw new MyException.NullDataException("You have to input course id deleted.");
        }
        ClientServer.Id requestId = ClientServer.Id.newBuilder().setId(courseId).build();
        ClientServer.Status responseStatus = stub.deleteCourse(requestId);
        responseStatus(responseStatus, "DELETE SUCCESS !!!");
    }

    private void registerCourse() {
        
    }

    private void showRegister() {
        
    }

    private void responseStatus(ClientServer.Status status, String successMessage) throws MyException{
        if(status.getStatus() == 201) {
            System.out.println(successMessage);
        } else if(status.getStatus() == 409) {
            throw new MyException.DuplicationDataException(status.getMessage());
        } else {
            throw new MyException(status.getMessage());
        }
    }

}
