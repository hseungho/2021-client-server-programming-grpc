import entity.Course;
import vo.CourseVO;

import java.util.List;
import java.util.stream.Collectors;

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

    public static CourseVO toVO(ClientServer.Course courseDto) {
        return new CourseVO(
                courseDto.getId(),
                courseDto.getCourseId(),
                courseDto.getProfName(),
                courseDto.getCourseName(),
                courseDto.getPrerequisiteList().stream()
                        .map(CourseDtoConverter::toVO).collect(Collectors.toSet())
        );
    }

    public static CourseVO toVO(entity.Course courseEntity) {
        return new CourseVO(
                courseEntity.getId(),
                courseEntity.getCourseId(),
                courseEntity.getProfName(),
                courseEntity.getCourseName(),
                courseEntity.getPrerequisite().stream()
                        .map(CourseDtoConverter::toVO).collect(Collectors.toSet())
        );
    }

}
