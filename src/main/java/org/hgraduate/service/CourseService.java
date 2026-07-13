package org.hgraduate.service;

import org.hgraduate.model.Category;
import org.hgraduate.model.Course;
import org.hgraduate.repository.CourseRepository;

import java.util.List;

public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void register(Course course)          // 중복 체크 포함 등록
    {

    }

    public List<Course> getAllCourses() {
        return null;
    }

    public Course getCourse(Long id)              // 없으면 예외
    {
        return null;
    }

    public void updateCourse(Course course) {

    }

    public void deleteCourse(Long id) {

    }

    public List<Course> searchByCategory(Category category) {
        return null;
    }

    public List<Course> searchByKeyword(String keyword) {
        return null;
    }
}