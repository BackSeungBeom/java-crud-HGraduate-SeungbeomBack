package org.hgraduate.repository;

import org.hgraduate.model.Category;
import org.hgraduate.model.Course;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseMemoryRepository implements CourseRepository {

    private final List<Course> courses = new ArrayList<>();

    @Override
    public void save(Course course) {
        long maxId = courses.stream()
                .mapToLong(Course::getId)
                .max()
                .orElse(0L);

        course.setId(maxId + 1);
        course.setCreatedAt(LocalDateTime.now());
        courses.add(course);
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses); // 원본 리스트 보호를 위해 복사본 반환
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void update(Course course) {
        Course existing = findById(course.getId())
                .orElseThrow(() -> new IllegalArgumentException("과목을 찾을 수 없습니다. id=" + course.getId()));

        existing.setCourseCode(course.getCourseCode());
        existing.setCourseName(course.getCourseName());
        existing.setCategory(course.getCategory());
        existing.setCredit(course.getCredit());
        existing.setSemester(course.getSemester());
        existing.setStatus(course.getStatus());
        existing.setGrade(course.getGrade());
        // id, createdAt은 수정 대상이 아니므로 건드리지 않음
    }

    @Override
    public void delete(Long id) {
        courses.removeIf(c -> c.getId().equals(id));
    }

    @Override
    public List<Course> findByCategory(Category category) {
        return courses.stream()
                .filter(c -> c.getCategory() == category)
                .toList();
    }

    @Override
    public List<Course> findByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return courses.stream()
                .filter(c -> c.getCourseName().toLowerCase().contains(lowerKeyword))
                .toList();
    }

    @Override
    public boolean existsByCourseCode(String courseCode) {
        return courses.stream()
                .anyMatch(c -> c.getCourseCode().equals(courseCode));
    }
}