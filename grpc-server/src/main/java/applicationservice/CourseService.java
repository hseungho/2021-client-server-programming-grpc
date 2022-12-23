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
        try {
            courseRepository.findByCourseId(courseVO.getCourseId())
                    .ifPresent(c -> {
                        throw new RuntimeException();
                    });
        } catch (RuntimeException e) {
            throw new MyException.DuplicationDataException("This course id is duplicated");
        }
        Course course = Course.createEntity(courseVO);
        courseRepository.save(course);
    }
}
