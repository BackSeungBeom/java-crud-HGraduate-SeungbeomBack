package org.hgraduate.repository;

import org.hgraduate.model.Category;
import org.hgraduate.model.Course;

import java.util.List;
import java.util.Optional;

public class CourseDbRepository implements CourseRepository{
    @Override
    public void save(Course course) {

    }

    @Override
    public List<Course> findAll() {
        return List.of();
    }

    @Override
    public Optional<Course> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void update(Course course) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Course> findByCategory(Category category) {
        return List.of();
    }

    @Override
    public List<Course> findByKeyword(String keyword) {
        return List.of();
    }

    @Override
    public boolean existsByCourseCode(String courseCode) {
        return false;
    }
}
