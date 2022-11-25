import com.google.protobuf.Empty;
import entity.Student;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;
import java.util.Scanner;

public class ClientGrpc {

    public static ClientGrpc instance;
    public static ClientGrpc getInstance() {
        return instance != null ? instance : new ClientGrpc();
    }

    private Scanner sc;
    private ManagedChannel channel;
    private ClientServerProtoGrpc.ClientServerProtoBlockingStub stub;
    private boolean isRunning;

    public ClientGrpc() {
        sc = new Scanner(System.in);
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

    public void start() {
        isRunning = true;

        while(isRunning) {
            printMenu();
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("******************** MENU *********************");
        System.out.println("1. List Students");
        System.out.println("2. List Courses");
        System.out.println("3. Add entity.Student");
        System.out.println("4. Delete entity.Student");
        System.out.println("5. Add entity.Course");
        System.out.println("6. Delete entity.Course");
        System.out.println("7. Make Reservation");
        System.out.println("8. List Reservation");
        System.out.println("x. Exit");
        System.out.print("메뉴 입력: ");
        this.selectMenu();
    }

    private void selectMenu() {
        String s = sc.next().toLowerCase();
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
            default -> {}
        }
    }

    private void showStudentList() {
        ClientServer.StudentList studentList;
        studentList = stub.getAllStudentData(Empty.newBuilder().build());
        switch (studentList.getStatus()) {
            case "SUCCESS" -> printStudent(studentList.getStudentList());
            case "FAILED_NO_DATA" -> {}
            default -> {}
        }
    }

    private void printStudent(List<ClientServer.Student> studentList) {
        System.out.println("<<<<<<<<<<<<<<   entity.Student List   >>>>>>>>>>>>>>");
        for(ClientServer.Student student_proto : studentList) {
            Student student = Student.toEntity(student_proto);
            System.out.println(student);
        }
    }

    private void showCourseList() {
    }

    private void addStudent() {
    }

    private void deleteStudent() {
        
    }

    private void addCourse() {
        
    }

    private void deleteCourse() {
        
    }

    private void registerCourse() {
        
    }

    private void showRegister() {
        
    }

}
