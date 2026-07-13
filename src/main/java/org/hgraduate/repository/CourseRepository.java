package org.hgraduate.repository;

import org.hgraduate.model.Category;
import org.hgraduate.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    void save(Course course);

    List<Course> findAll();

    Optional<Course> findById(Long id);

    void update(Course course);

    void delete(Long id);

    List<Course> findByCategory(Category category);   // 검색 기능 1

    List<Course> findByKeyword(String keyword);        // 검색 기능 2 (과목명 검색)

    boolean existsByCourseCode(String courseCode);     // 중복 체크용
}