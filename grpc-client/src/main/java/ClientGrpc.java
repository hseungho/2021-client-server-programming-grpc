import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class ClientGrpc {

    public static ClientGrpc instance;
    public static ClientGrpc getInstance() {
        return instance != null ? instance : new ClientGrpc();
    }

    private BufferedReader br;
    private ManagedChannel channel;
    private ClientServerProtoGrpc.ClientServerProtoBlockingStub stub;
    private boolean isRunning;

    public ClientGrpc() {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
    }

    private void init() {
        initChannel();
        initStub();
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
                    default -> System.out.println("Incorrect Menu !!");
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
        StringBuilder result = new StringBuilder();
        for(ClientServer.Student student_proto : studentList) {
            Student student = Student.toEntity(student_proto);
            result.append(student).append("\n");
        }
        System.out.println(result);
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
        StringBuilder result = new StringBuilder();
        for(ClientServer.Course course_proto : courseList) {
            Course course = Course.toEntity(course_proto);
            result.append(course).append("\n");
        }
        System.out.println(result);
    }

    private void addStudent() throws IOException, MyException {
        System.out.println("<<<<<<<<<<<<<<   Add Student   >>>>>>>>>>>>>>");
        System.out.println("------Student Information------");
        System.out.print("Student ID: "); String studentId = br.readLine().trim();
        System.out.print("Student Name: "); String studentName = br.readLine().trim();
        System.out.print("Student Department: "); String studentDept = br.readLine().trim();
        System.out.print("Student Completed Course List: "); String completedCourse = br.readLine().trim();
        if(studentId.isBlank() || studentName.isBlank() || studentDept.isBlank()) {
            throw new MyException.NullDataException("You have to input every student information.");
        }
        List<String> completedCourseList = List.of(completedCourse.split(" "));

        ClientServer.Student student = ClientServer.Student.newBuilder()
                .setId(studentId)
                .setName(studentName)
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
        List<String> prerequisiteList = List.of(prerequisite.split(" "));

        ClientServer.Course course = ClientServer.Course.newBuilder()
                .setId(courseId)
                .setProfName(profName)
                .setCourseName(courseName)
                .addAllPrerequisite(prerequisiteList).build();
        ClientServer.Status status = stub.addCourse(course);
        responseStatus(status, "ADD SUCCESS !!!");
    }

    private void deleteCourse() {
        
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
