package repository;

import config.Repository;
import entity.Course;

import java.util.Optional;

public class CourseRepository extends Repository<Course, Long> {

    public CourseRepository() {
        super(Course.class);
    }

    private final String f_course_id = "courseId";

    public Optional<Course> findByCourseId(String courseId) {
        return super.findByStringCondition(f_course_id, courseId);
    }

    public void deleteByCourseId(String courseId) {
        super.deleteByStringCondition(f_course_id, courseId);
    }
}
