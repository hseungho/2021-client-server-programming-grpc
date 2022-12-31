package applicationservice;

import dto.request.CourseCreateRequest;
import entity.Course;
import exception.DatabaseException;
import exception.LMSException;
import exception.NotFoundCourseIdException;
import exception.NotFoundStudentIdException;
import repository.CourseRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourseList() {
        return courseRepository.findAll();
    }

    public void addCourse(CourseCreateRequest courseCreateRequest) {
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
                    throw new LMSException("This course id is duplicated");
                });
        Set<Course> prerequisites = course.getPrerequisite().stream().map(pre_course ->
                courseRepository.findByCourseId(pre_course.getCourseId())
                        .orElseThrow(NotFoundCourseIdException::new)
        ).collect(Collectors.toSet());
        course.setPrerequisite(prerequisites);
        courseRepository.save(course);
    }

    public void deleteCourse(String courseId) {
        try {
            courseRepository.deleteByCourseId(courseId);

        } catch (DatabaseException.NotExistException e) {
            System.err.println("LOG: "+e.getMessage());
            throw new NotFoundStudentIdException();

        } catch (DatabaseException e) {
            System.err.println("LOG: "+e.getMessage());
            throw new LMSException("Sorry, there was an error on the server. we'll figure it out in a minute");
        }
    }

}
