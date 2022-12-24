package applicationservice;

import entity.Course;
import exception.MyException;
import repository.CourseRepository;
import vo.CourseVO;

import java.util.List;

public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService() {
        this.courseRepository = new CourseRepository();
    }

    public List<Course> getAllCourseList() {
        return courseRepository.findAll();
    }

    public void addCourse(CourseVO courseVO) throws MyException.DuplicationDataException{
        Course course = Course.createEntity(courseVO);
        addCourse(course);
    }

    public void addCourse(Course course) {
        courseRepository.findByCourseId(course.getCourseId()).stream().findAny()
                .ifPresent(c -> {
                    throw new MyException.DuplicationDataException("This course id is duplicated");
                });
        courseRepository.save(course);
    }

    public void deleteCourse(String courseId) {
        if(courseRepository.findByCourseId(courseId).isEmpty()) {
            throw new MyException.InvalidedDataException("This course id doesn't exist");
        }
        courseRepository.deleteByCourseId(courseId);
    }

}
