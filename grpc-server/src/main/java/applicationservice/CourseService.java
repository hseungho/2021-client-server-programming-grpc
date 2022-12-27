package applicationservice;

import dto.request.CourseCreateRequest;
import entity.Course;
import exception.MyException;
import exception.NotFoundCourseIdException;
import repository.CourseRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService() {
        this.courseRepository = new CourseRepository();
    }

    public List<Course> getAllCourseList() {
        return courseRepository.findAll();
    }

    public void addCourse(CourseCreateRequest courseCreateRequest) throws MyException.DuplicationDataException{
        Course course = Course.createEntity(courseCreateRequest);
        if(!courseCreateRequest.getPrerequisiteIds().isEmpty()) {
            Set<Course> preCourses = courseCreateRequest.getPrerequisiteIds().stream()
                    .map(courseId -> courseRepository.findByCourseId(courseId)
                            .orElseThrow(NotFoundCourseIdException::new))
                    .collect(Collectors.toSet());
            course.setPrerequisite(preCourses);
        }
        addCourse(course);
    }

    public void addCourse(Course course) {
        courseRepository.findByCourseId(course.getCourseId()).stream().findAny()
                .ifPresent(c -> {
                    throw new MyException.DuplicationDataException("This course id is duplicated");
                });
        Set<Course> prerequisites = course.getPrerequisite().stream().map(pre_course ->
                courseRepository.findByCourseId(pre_course.getCourseId())
                        .orElseThrow(NotFoundCourseIdException::new)
        ).collect(Collectors.toSet());
        course.setPrerequisite(prerequisites);
        courseRepository.save(course);
    }

    public void deleteCourse(String courseId) {
        if(courseRepository.findByCourseId(courseId).isEmpty()) {
            throw new MyException.InvalidedDataException("This course id doesn't exist");
        }
        courseRepository.deleteByCourseId(courseId);
    }

}
