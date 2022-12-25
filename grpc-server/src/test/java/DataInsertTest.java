import entity.Course;
import entity.Student;
import repository.CourseRepository;
import repository.StudentRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class DataInsertTest {

//    @Test
    public void 학생_데이터_초기화() throws IOException {
        InputStream is = getClass().getResourceAsStream("Students.txt");
        BufferedReader objStudentFile = new BufferedReader(new InputStreamReader(is));
        List<Student> students = new ArrayList<>();
        while(objStudentFile.ready()) {
            String studentInfo = objStudentFile.readLine();
            if(!studentInfo.isBlank()) {
                StringTokenizer stk = new StringTokenizer(studentInfo);
                String studentId = stk.nextToken();
                String firstName = stk.nextToken();
                String lastName = stk.nextToken();
                String department = stk.nextToken();
                students.add(new Student(studentId, firstName, lastName, department, null));
            }
        }
        objStudentFile.close();

        StudentRepository studentRepository = new StudentRepository();
        students.stream().forEach(student -> studentRepository.save(student));
    }

//    @Test
    public void 강의_데이터_초기화() throws IOException {
        InputStream is = getClass().getResourceAsStream("Courses.txt");
        BufferedReader objStudentFile = new BufferedReader(new InputStreamReader(is));
        List<Course> courses = new ArrayList<>();
        while(objStudentFile.ready()) {
            String courseInfo = objStudentFile.readLine();
            if(!courseInfo.isBlank()) {
                StringTokenizer stk = new StringTokenizer(courseInfo);
                String courseId = stk.nextToken();
                String profName = stk.nextToken();
                String courseName = stk.nextToken();
                courses.add(new Course(courseId, profName, courseName, null));
            }
        }
        objStudentFile.close();

        CourseRepository courseRepository = new CourseRepository();
        courses.stream().forEach(course -> courseRepository.save(course));
    }

//    @Test
    public void 학생_이수과목_데이터_초기화() throws IOException {
        InputStream is = getClass().getResourceAsStream("Students.txt");
        BufferedReader objStudentFile = new BufferedReader(new InputStreamReader(is));
        Map<String, List<String>> studentMap = new HashMap<>();
        while(objStudentFile.ready()) {
            String studentInfo = objStudentFile.readLine();
            if(!studentInfo.isBlank()) {
                StringTokenizer stk = new StringTokenizer(studentInfo);
                String studentId = stk.nextToken();
                stk.nextToken();
                stk.nextToken();
                stk.nextToken();
                List<String> completedCourses = new ArrayList<>();
                while(stk.hasMoreTokens()) {
                    completedCourses.add(stk.nextToken());
                }
                studentMap.put(studentId, completedCourses);
            }
        }
        objStudentFile.close();

        StudentRepository studentRepository = new StudentRepository();
        Set<String> studentIds = studentMap.keySet();
        List<Student> students = studentIds.stream()
                .map(studentRepository::findByStudentId)
                .map(Optional::orElseThrow)
                .toList();

        CourseRepository courseRepository = new CourseRepository();
        students.stream().forEach(student -> {
            List<String> courseIds = studentMap.get(student.getStudentId());
            Set<Course> courses = courseIds.stream()
                    .map(courseRepository::findByCourseId)
                    .map(Optional::orElseThrow)
                    .collect(Collectors.toSet());
            student.setCompletedCourseList(courses);
            studentRepository.save(student);
        });
    }

//    @Test
    public void 강의_선수과목_데이터_초기화() throws IOException {
        InputStream is = getClass().getResourceAsStream("Courses.txt");
        BufferedReader objStudentFile = new BufferedReader(new InputStreamReader(is));
        Map<String, List<String>> courseMap = new HashMap<>();
        while(objStudentFile.ready()) {
            String courseInfo = objStudentFile.readLine();
            if(!courseInfo.isBlank()) {
                StringTokenizer stk = new StringTokenizer(courseInfo);
                String courseId = stk.nextToken();
                stk.nextToken();
                stk.nextToken();
                List<String> prerequisites = new ArrayList<>();
                while(stk.hasMoreTokens()) {
                    prerequisites.add(stk.nextToken());
                }
                courseMap.put(courseId, prerequisites);
            }
        }
        objStudentFile.close();

        CourseRepository courseRepository = new CourseRepository();
        Set<String> courseIds = courseMap.keySet();
        List<Course> courses = courseIds.stream()
                .map(courseRepository::findByCourseId)
                .map(Optional::orElseThrow)
                .toList();
        courses.stream()
                .forEach(course -> {
                    List<String> preIds = courseMap.get(course.getCourseId());
                    Set<Course> preSet = preIds.stream()
                            .map(courseRepository::findByCourseId)
                            .map(Optional::orElseThrow)
                            .collect(Collectors.toSet());
                    course.setPrerequisite(preSet);
                    courseRepository.save(course);
                });
    }
}
