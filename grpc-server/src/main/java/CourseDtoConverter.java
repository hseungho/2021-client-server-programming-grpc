import dto.request.CourseCreateRequest;
import entity.Course;

import java.util.List;

public class CourseDtoConverter {

    public static ClientServer.CourseList toProtoCourseList(List<Course> courses) {
        ClientServer.CourseList protoCourseList;
        List<ClientServer.Course> protoCourses = courses.stream()
                .map(CourseDtoConverter::toProtoCourse).toList();
        protoCourseList = ClientServer.CourseList.newBuilder()
                .setStatus("SUCCESS")
                .addAllCourse(protoCourses)
                .build();
        return protoCourseList;
    }

    public static ClientServer.Course toProtoCourse(Course course) {
        return ClientServer.Course.newBuilder()
                .setId(course.getId())
                .setCourseId(course.getCourseId())
                .setProfName(course.getProfName())
                .setCourseName(course.getCourseName())
                .addAllPrerequisite(
                        course.getPrerequisite().stream()
                                .map(CourseDtoConverter::toProtoCourse)
                                .toList()
                )
                .build();
    }

    public static CourseCreateRequest toCreateRequest(ClientServer.Course courseDto) {
        return new CourseCreateRequest(
                courseDto.getCourseId(),
                courseDto.getProfName(),
                courseDto.getCourseName(),
                courseDto.getPrerequisiteList().stream()
                                .map(ClientServer.Course::getCourseId)
                                        .toList()
        );
    }

}
