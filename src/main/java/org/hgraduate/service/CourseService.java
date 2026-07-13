package org.hgraduate.service;

import org.hgraduate.exception.DuplicateCourseCodeException;
import org.hgraduate.model.Category;
import org.hgraduate.model.Course;
import org.hgraduate.repository.CourseRepository;

import java.util.List;

public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void register(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new DuplicateCourseCodeException(course.getCourseCode());
        }
        courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("과목을 찾을 수 없습니다. id=" + id));
    }

    public void updateCourse(Course course) {
        courseRepository.update(course);
    }

    public void deleteCourse(Long id) {
        getCourse(id);
        courseRepository.delete(id);
    }

    public List<Course> searchByCategory(Category category) {
        return courseRepository.findByCategory(category);
    }

    public List<Course> searchByKeyword(String keyword) {
        return courseRepository.findByKeyword(keyword);
    }
}