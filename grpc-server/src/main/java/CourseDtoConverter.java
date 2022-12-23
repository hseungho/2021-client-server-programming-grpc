import entity.Course;

public class CourseDtoConverter {

    public static ClientServer.Course toProtoCourseDto(Course course) {
        return ClientServer.Course.newBuilder()
                .setId(course.getId())
                .setCourseId(course.getCourseId())
                .setProfName(course.getProfName())
                .setCourseName(course.getCourseName())
                .addAllPrerequisite(
                        course.getPrerequisite().stream()
                                .map(CourseDtoConverter::toProtoCourseDto)
                                .toList()
                )
                .build();
    }

    public static Course toEntity(ClientServer.Course courseDto) {
        return new Course(
                courseDto.getId(),
                courseDto.getCourseId(),
                courseDto.getProfName(),
                courseDto.getCourseName(),
                courseDto.getPrerequisiteList().stream()
                        .map(CourseDtoConverter::toEntity).toList()
        );
    }
}
