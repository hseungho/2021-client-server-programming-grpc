import com.google.protobuf.Empty;
import dto.CourseDto;
import dto.StudentDto;
import exception.MyException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import util.CommandLineTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
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
        studentTable.setShowVerticalLines(true);
        studentTable.setHeaders("Student ID", "First Name", "Last Name" ,"Department", "Completed Course ID");

        courseTable = new CommandLineTable();
        courseTable.setShowVerticalLines(true);
        courseTable.setHeaders("Course ID", "Prof. Name", "Course Name", "Prerequisite ID");
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

        response(studentList.getStatus());

        printStudent(studentList.getStudentList());
    }

    private void printStudent(List<ClientServer.Student> studentList) {
        studentTable.resetRows();
        List<StudentDto> studentDtos = studentList.stream().map(student -> new StudentDto(
                student.getId(), student.getStudentId(), student.getFirstName(), student.getLastName(), student.getDepartment(),
                student.getCompletedCourseListList().stream().map(ClientServer.Course::getCourseId).toList())
        ).toList();
        studentDtos.forEach(studentDto -> {
            StringBuilder completedCourseId = new StringBuilder();
            studentDto.getCompletedCourseIds().forEach(s -> completedCourseId.append(s).append(" "));
            studentTable.addRow(
                    studentDto.getStudentId(), studentDto.getFirstName(), studentDto.getLastName(), studentDto.getDepartment(),
                    completedCourseId.toString());
        });
        studentTable.print();
    }

    private void showCourseList() {
        ClientServer.CourseList courseList = stub.getAllCourseData(Empty.newBuilder().build());

        response(courseList.getStatus());

        printCourse(courseList.getCourseList());
    }

    private void printCourse(List<ClientServer.Course> courseList) {
        courseTable.resetRows();
        List<CourseDto> courseDtos = courseList.stream().map(course -> new CourseDto(
                course.getId(), course.getCourseId(), course.getProfName(), course.getCourseName(),
                course.getPrerequisiteList().stream().map(ClientServer.Course::getCourseId).toList()
        )).toList();
        courseDtos.forEach(courseDto -> {
            StringBuilder prerequisiteId = new StringBuilder();
            courseDto.getPrerequisiteIds().forEach(c -> prerequisiteId.append(c).append(" "));
            courseTable.addRow(
                    courseDto.getCourseId(), courseDto.getProfName(), courseDto.getCourseName(),
                    prerequisiteId.toString()
            );
        });
        courseTable.print();
    }

    private void addStudent() throws IOException, MyException {
        System.out.println("<<<<<<<<<<<<<<   Add Student   >>>>>>>>>>>>>>");
        System.out.print("Student ID: "); String studentId = br.readLine().trim();
        System.out.print("Student First Name: "); String studentFirstName = br.readLine().trim();
        System.out.print("Student Last Name: "); String studentLastName = br.readLine().trim();
        System.out.print("Student Department: "); String studentDept = br.readLine().trim();
        System.out.print("Student Completed Course List: "); String completedCourse = br.readLine().trim();

        validateInputValue(List.of("Student ID", "First Name", "Last Name", "Department"),
                studentId, studentFirstName, studentLastName, studentDept);

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

        response(status);
    }

    private void deleteStudent() throws IOException, MyException {
        System.out.println("<<<<<<<<<<<<<<   Delete Student   >>>>>>>>>>>>>>");
        System.out.print("Student ID: "); String studentId = br.readLine().trim();

        validateInputValue(List.of("Student ID"), studentId);

        ClientServer.Id requestId = ClientServer.Id.newBuilder().setId(studentId).build();

        ClientServer.Status status = stub.deleteStudent(requestId);

        response(status);
    }

    private void addCourse() throws IOException, MyException {
        System.out.println("<<<<<<<<<<<<<<   Add Course   >>>>>>>>>>>>>>");
        System.out.print("Course ID: "); String courseId = br.readLine().trim();
        System.out.print("Prof Name: "); String profName = br.readLine().trim();
        System.out.print("Course Name: "); String courseName = br.readLine().trim();
        System.out.print("Course Prerequisite List: "); String prerequisite = br.readLine().trim();

        validateInputValue(List.of("Course ID", "Prof Name", "Course Name"),
                courseId, profName, courseName);

        List<String> strPrerequisiteList = List.of(prerequisite.split(" "));

        List<ClientServer.Course> prerequisiteList = strPrerequisiteList.stream().map(preCourseId ->
                ClientServer.Course.newBuilder().setCourseId(preCourseId).build()).toList();

        ClientServer.Course course = ClientServer.Course.newBuilder()
                .setCourseId(courseId)
                .setProfName(profName)
                .setCourseName(courseName)
                .addAllPrerequisite(prerequisiteList).build();

        ClientServer.Status status = stub.addCourse(course);

        response(status);
    }

    private void deleteCourse() throws IOException, MyException {
        System.out.println("<<<<<<<<<<<<<<   Delete Course   >>>>>>>>>>>>>>");
        System.out.print("Course ID: "); String courseId = br.readLine().trim();

        validateInputValue(List.of("Course ID"), courseId);

        ClientServer.Id requestId = ClientServer.Id.newBuilder().setId(courseId).build();

        ClientServer.Status status = stub.deleteCourse(requestId);

        response(status);
    }

    private void registerCourse() throws IOException {
        System.out.println("<<<<<<<<<<<<<<   Make Reservation   >>>>>>>>>>>>>>");
        System.out.print("Student ID: "); String studentId = br.readLine().trim();
        System.out.print("Course ID: "); String courseId = br.readLine().trim();

        validateInputValue(List.of("Student ID", "Course ID"), studentId, courseId);

        ClientServer.Register registerRequest = ClientServer.Register.newBuilder()
                .setStudentId(studentId)
                .setCourseId(courseId)
                .build();

        ClientServer.Status status = stub.register(registerRequest);

        response(status);
    }

    private void showRegister() throws IOException {
        System.out.println("<<<<<<<<<<<<<<   List Reservation   >>>>>>>>>>>>>>");
        System.out.print("Student ID: "); String studentId = br.readLine().trim();

        ClientServer.CourseList courseList = stub.getAllRegisterData(
                ClientServer.Id.newBuilder().setId(studentId).build()
        );

        response(courseList.getStatus());

        printCourse(courseList.getCourseList());
    }

    private void response(ClientServer.Status status) {
        errorResponse(status);
        successResponse(status);
    }

    private void successResponse(ClientServer.Status status) {
        if(!status.getMessage().isBlank()) {
            System.out.println(status.getMessage());
        }
    }

    private void errorResponse(ClientServer.Status status) {
        if(status.getCode() >= HttpResponseCode.BAD_REQUEST.getCode()) {
            throw new MyException(status.getMessage());
        }
    }

    private void validateInputValue(List<String> inputType, String... inputs) {
        Arrays.stream(inputs).forEach(input -> {
            if (input.isBlank()) {
                StringBuilder typeBuilder = new StringBuilder();
                inputType.forEach(type -> typeBuilder.append(type).append(" "));
                throw new MyException.NullDataException("You have to input all necessary information\nnecessary : " + typeBuilder);
            }
        });
    }
}
