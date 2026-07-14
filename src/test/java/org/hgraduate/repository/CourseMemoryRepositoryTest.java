package org.hgraduate.repository;

import org.hgraduate.model.Category;
import org.hgraduate.model.Course;
import org.hgraduate.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseMemoryRepositoryTest {

    private CourseRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CourseMemoryRepository();
    }

    private Course sampleCourse() {
        return new Course("CS101", "자료구조", Category.MAJOR_REQUIRED,
                3, "2024-1", Status.COMPLETED, "A+");
    }

    @Test
    void save_assignsIdAndPersists() {
        Course course = sampleCourse();

        repository.save(course);

        assertEquals(1L, course.getId());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findById_returnsSavedCourse() {
        Course course = sampleCourse();
        repository.save(course);

        Optional<Course> found = repository.findById(course.getId());

        assertTrue(found.isPresent());
        assertEquals("CS101", found.get().getCourseCode());
    }

    @Test
    void findById_returnsEmpty_whenNotExists() {
        Optional<Course> found = repository.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void update_changesExistingCourse() {
        Course course = sampleCourse();
        repository.save(course);

        Course updated = repository.findById(course.getId()).get();
        updated.setCourseName("자료구조와알고리즘");
        updated.setCredit(4);
        repository.update(updated);

        Course result = repository.findById(course.getId()).get();
        assertEquals("자료구조와알고리즘", result.getCourseName());
        assertEquals(4, result.getCredit());
    }

    @Test
    void delete_removesCourse() {
        Course course = sampleCourse();
        repository.save(course);

        repository.delete(course.getId());

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void findByCategory_filtersCorrectly() {
        repository.save(sampleCourse());
        repository.save(new Course("GE101", "글쓰기", Category.GENERAL_REQUIRED,
                2, "2024-1", Status.PLANNED, null));

        List<Course> majors = repository.findByCategory(Category.MAJOR_REQUIRED);

        assertEquals(1, majors.size());
        assertEquals("CS101", majors.get(0).getCourseCode());
    }

    @Test
    void findByKeyword_matchesPartialCourseName() {
        repository.save(sampleCourse());

        List<Course> result = repository.findByKeyword("자료");

        assertEquals(1, result.size());
    }

    @Test
    void existsByCourseCode_detectsDuplicate() {
        repository.save(sampleCourse());

        assertTrue(repository.existsByCourseCode("CS101"));
        assertFalse(repository.existsByCourseCode("CS999"));
    }
}
