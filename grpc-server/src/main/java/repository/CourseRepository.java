package repository;

import config.Repository;
import entity.Course;

import java.util.Optional;

public class CourseRepository extends Repository<Course, Long> {

    public CourseRepository() {
        super(Course.class);
    }
    public Optional<Course> findByCourseId(String courseId) {
        return super.findByStringField("courseId", courseId);
    }
}
